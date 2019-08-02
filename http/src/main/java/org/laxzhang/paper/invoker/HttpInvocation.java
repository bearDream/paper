package org.laxzhang.paper.invoker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.laxzhang.common.KM;
import org.laxzhang.paper.Http;
import org.laxzhang.paper.PaperClient;
import org.laxzhang.paper.bridge.IRequestBody;
import org.laxzhang.paper.bridge.IRequestQuery;
import org.laxzhang.paper.bridge.RequestBody;
import org.laxzhang.paper.bridge.RequestQuery;
import org.laxzhang.paper.handler.ParamSerializer;
import org.laxzhang.paper.support.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 18:18
 */
public class HttpInvocation implements Invocation{

	private static final long serialVersionUID = -3939611397544570435L;

	private final static Logger logger = LoggerFactory.getLogger(HttpInvocation.class);

	private Http http;
	private Invoker<?> invoker;
	private IRequestQuery requestQuery;
	private IRequestBody requestBody;
	private ParamSerializer paramSerializer;
	private Object[] arguments;
	private Method method;
	private Map<String, Object> args = new HashMap<>();
	private Map<String, String> head = new HashMap<>();

	/**
	 * @param execution
	 * @param method
	 * @param args
	 */
	public HttpInvocation(Execution<?> execution, Method method, Object[] args) {
		Http ht = method.getAnnotation(Http.class);
		String url = assemblePath(execution.getServer(), ht.path());
		this.requestQuery = new RequestQuery(url, ht.urlencode());
		this.paramSerializer = execution.getHttpSerializer();
		this.arguments = args;
		this.method = method;
		this.http = ht;
		this.invoker = execution.getInvoker();

		Parameter[] paras = method.getParameters();

		PaperClient remote = annotationOf(PaperClient.class, 0);
		if (KM.isEmpty(paras)){
			this.requestBody = new RequestBody(remote, http, paramSerializer, this.args);
			return;
		}

		int style = remote.style();
		for (int i = 0; i < paras.length; i++) {
			if (arguments[i] == null){
				continue;
			}
			if (Http.WRAP == http.body() || http.request().oneOf(Http.Content.XML) || KM.isBasicType(paras[i].getType())){
				String key = Cache.CacheHolder.getCache().resolveKey(style, paras[i]);
				Object val = Cache.CacheHolder.getCache().resolveValue(args[i], paras[i]);
				this.args.put(key, val);
			}
			this.args.putAll(Cache.CacheHolder.getCache().map(style, args[i]));
		}

		this.requestBody = new RequestBody(remote, ht, paramSerializer, this.args);
	}

	/**
	 * assemble root and path.
	 * @param root
	 * @param path
	 * @return
	 */
	private String assemblePath(String root, String path) {
		boolean isFullPath = !KM.isBlank(path) && (path.startsWith("http://") && (path.startsWith("https://")));
		if (isFullPath){
			return path;
		}
		// For example: root like http://www.google.com/ and path like /login
		boolean repeat = root.endsWith("/") && path.startsWith("/");
		// For example: root like http://www.google.com and path like: login, then need to add / in their middle.
		boolean bothLack = !KM.isBlank(path) && !root.endsWith("/") && !path.startsWith("/");
		return (repeat ? (root + path.substring(1)) : (bothLack ? root + "/" + path : root + path)).intern();
	}

	@Override
	public Invoker<?> getProxy() {
		return this.invoker;
	}

	@Override
	public Class<?> getInterface() {
		return this.method.getDeclaringClass();
	}

	@Override
	public Method getMethod() {
		return this.method;
	}

	@Override
	public Object[] getParameters() {
		return this.arguments;
	}

	@Override
	public Type getType() {
		return this.method.getGenericReturnType();
	}

	@Override
	public <T extends Annotation> T annotationOf(Class<T> a, int level) {
		if (TYPE == level){
			return this.method.getDeclaringClass().getAnnotation(a);
		}
		return this.method.getAnnotation(a);
	}

	@Override
	public IRequestQuery url() {
		if (this.http.method().oneOf(Http.Method.POST, Http.Method.PUT) || KM.isEmpty(this.args)){
			return this.requestQuery;
		}
		IRequestQuery url = this.requestQuery.clone();
		this.args.entrySet().stream().forEach(kv -> url.addQuery(kv.getKey(), kv.getValue()));
		return url;
	}

	@Override
	public IRequestQuery addQuery(String key, Object value) {
		return this.requestQuery.addQuery(key, value);
	}

	@Override
	public IRequestBody addArgs(String key, Object value) {
		return this.requestBody.put(key, value);
	}

	@Override
	public IRequestBody getArgs() {
		return this.requestBody;
	}

	@Override
	public Map<String, String> getHeaders() {
		return this.head;
	}

	@Override
	public Type returnType() {
		return this.method.getGenericReturnType();
	}

	@Override
	public Map<String, String> addHeaders(String key, String value) {
		this.head.put(key, value);
		return this.head;
	}
}
