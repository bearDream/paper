package org.laxzhang.paper.invoker;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.laxzhang.common.KM;
import org.laxzhang.paper.Http;
import org.laxzhang.paper.PaperClient;
import org.laxzhang.paper.exceptions.PaperClientException;
import org.laxzhang.paper.handler.RootX;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 16:38
 */
public class HttpInvoker implements InvocationHandler {

	private final PaperClient client;
	private final Invoker<?> invoker;
	private final Map<PaperClient, Execution<?>> exeMap = new ConcurrentHashMap<>(1);
	// MethodInvoker中的lookup可以方便的去查找类
	private final static MethodHandles.Lookup lookup = MethodHandles.lookup();

	static {
		try {
			Field allowedModes = MethodHandles.Lookup.class.getDeclaredField("allowedModes");
			if (Modifier.isFinal(allowedModes.getModifiers())) {
				final Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				// Just get PRIVATE modifier.
				modifiersField.setInt(allowedModes, allowedModes.getModifiers() & ~Modifier.FINAL);
				allowedModes.setAccessible(true);
				allowedModes.set(lookup, -1);
			}
		} catch (Exception e) {
			//
		}
	}

	public HttpInvoker(PaperClient client, Invoker<?> invoker){
		this.client = client;
		this.invoker = invoker;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		Class<?> clazz = method.getDeclaringClass();
		if (clazz == Object.class){
			return method.invoke(this, args);
		}

		// If method is interface default method.
		if (method.isDefault()){
			return lookup.in(clazz).unreflectSpecial(method, clazz).bindTo(proxy).invokeWithArguments(args);
		}

		Http http = method.getAnnotation(Http.class);
		if (http == null){
			throw new IllegalStateException("PaperClient request method must annotated with @HTTP.");
		}

		PaperClient cl = clazz.getAnnotation(PaperClient.class);
		PaperClient remote = cl == null ? this.client : cl;
		if (KM.isBlank(remote.root()) && KM.isEmpty(remote.rootX()) && KM.isBlank(http.path())){
			throw new IllegalStateException("@PaperClient root, rootX and @Http path not be blank together.");
		}

		Execution<?> execution = getExecution(remote);
		Invocation invocation = new HttpInvocation(execution, method, args);
		try {
			return execution.execute(invocation);
		}catch (Exception e){
			if (e instanceof RuntimeException){
				throw e;
			}
			Class<?>[] es = method.getExceptionTypes();
			if (Arrays.stream(es).anyMatch(c -> c.isAssignableFrom(c.getClass()))){
				throw e;
			}
			throw new PaperClientException(e.getMessage(), e);
		}
	}

	private Execution<?> getExecution(PaperClient remote) {
		if (null == exeMap.get(remote)){
			exeMap.putIfAbsent(remote, createHttpExecution(remote));
		}
		return exeMap.get(remote);
	}

	private Execution<?> createHttpExecution(PaperClient remote) {
		String root = remote.root();
		// get real root path by rootX.
		if (!KM.isEmpty(remote.rootX())){
			RootX rootX = KM.newInstance(remote.rootX()[0]);
			root = rootX.root(root);
		}

		Execution<?> execution = HttpExecution.HttpExecutionBuilder.create(this.invoker)
				.baseUrl(root)
				.causeHandler(KM.isEmpty(remote.cause()) ? null : remote.cause()[0])
				.resultHandler(KM.isEmpty(remote.result()) ? null : remote.result()[0])
				.serialize(KM.isEmpty(remote.serializer()) ? null : remote.serializer()[0])
				.filter(remote.filter())
				.build();

		return execution;
	}
}
