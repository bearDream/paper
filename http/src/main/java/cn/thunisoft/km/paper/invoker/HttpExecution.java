package cn.thunisoft.km.paper.invoker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cn.thunisoft.km.common.KM;
import cn.thunisoft.km.paper.exceptions.PaperClientException;
import cn.thunisoft.km.paper.exceptions.RedirectException;
import cn.thunisoft.km.paper.filter.Filter;
import cn.thunisoft.km.paper.filter.HttpEchoFilter;
import cn.thunisoft.km.paper.filter.HttpLimitFilter;
import cn.thunisoft.km.paper.filter.HttpRetryFilter;
import cn.thunisoft.km.paper.filter.HttpValidationFilter;
import cn.thunisoft.km.paper.handler.CauseHandler;
import cn.thunisoft.km.paper.handler.DefaultCauseHandler;
import cn.thunisoft.km.paper.handler.DefaultParamSerialize;
import cn.thunisoft.km.paper.handler.DefaultResultHandler;
import cn.thunisoft.km.paper.handler.ParamSerializer;
import cn.thunisoft.km.paper.handler.ResultHandler;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/23 10:47
 */
public class HttpExecution implements Execution<Object>, Invoker<Object> {

	private String server = "http://localhost";
	private CauseHandler causeHandler = new DefaultCauseHandler();
	private ResultHandler resultHandler = new DefaultResultHandler();
	private ParamSerializer paramSerializer = new DefaultParamSerialize();
	private Invoker<?> chanInvoker;
	private Invoker<?> httpInvoker;

	/**
	 * According to filters, build invoker chain as for execute all filters.
	 * @param invoker
	 * @param filters
	 * @return
	 */
	private Invoker<?> buildInvokerChain(final Invoker<?> invoker, List<Filter> filters){
		if (filters.isEmpty()){
			return invoker;
		}

		// build chain.
		Invoker<?> last = invoker;
		for (int i = filters.size() - 1; i >= 0; i--) {
			final Filter filter = filters.get(i);
			final Invoker<?> next = last;
			last = new Invoker<Object>() {
				@Override
				public Object invoker(Invocation invocation) throws Throwable {
					return filter.invoke(next, invocation);
				}

				@Override
				public String toString() {
					return invoker.toString();
				}
			};
		}
		return last;
	}


	@Override
	public Object execute(Invocation invocation) throws Throwable {
		try {
			return chanInvoker.invoker(invocation);
		}catch (Exception e){
			return causeHandler.handler(invocation, e);
		}
	}

	@Override
	public Object invoker(Invocation invocation) throws Throwable {
		try {
			byte[] o = (byte[]) this.httpInvoker.invoker(invocation);
			return this.resultHandler.handlerPrint(invocation, o);
		}catch (RedirectException e){
			return this.causeHandler.handlerRedirect(invocation, e);
		}
	}

	@Override
	public Invoker<?> getInvoker() {
		return this.httpInvoker;
	}

	@Override
	public String getServer() {
		return this.server;
	}

	@Override
	public ParamSerializer getHttpSerializer() {
		return this.paramSerializer;
	}

	@Override
	public CauseHandler getCauseHandler() {
		return this.causeHandler;
	}

	@Override
	public ResultHandler getResultHandler() {
		return this.resultHandler;
	}

	/**
	 * static Http execution builder.
	 * Just Builder Design Pattern.
	 */
	final static class HttpExecutionBuilder{

		private HttpExecution e = new HttpExecution();
		private List<Class<? extends Filter>> fs = new ArrayList<>();

		private HttpExecutionBuilder(Invoker<?> invoker){
			this.e.httpInvoker = invoker;
		}

		public static HttpExecutionBuilder create(Invoker<?> invoker){
			return new HttpExecutionBuilder(invoker);
		}

		public HttpExecutionBuilder baseUrl(String url){
			this.e.server = url;
			return this;
		}

		public HttpExecutionBuilder causeHandler(Class<? extends CauseHandler> handler){
			if (null == handler || handler.isInterface()){
				return this;
			}
			this.e.causeHandler = KM.newInstance(handler);
			return this;
		}

		public HttpExecutionBuilder resultHandler(Class<? extends ResultHandler> handler){
			if (null == handler || handler.isInterface()){
				return this;
			}
			this.e.resultHandler = KM.newInstance(handler);
			return this;
		}

		public HttpExecutionBuilder filter(Class<? extends Filter>[] filters){
			if (KM.isEmpty(filters)){
				return this;
			}
			fs.addAll(Arrays.asList(filters));
			return this;
		}

		public HttpExecutionBuilder serialize(Class<? extends ParamSerializer> serialize){
			if (null == serialize || serialize.isInterface()){
				return this;
			}
			this.e.paramSerializer = KM.newInstance(serialize);
			return this;
		}

		public Execution<?> build(){
			if (null == this.e.httpInvoker){
				throw new PaperClientException("Must appoint http invoker implements.");
			}
			this.fs.removeIf(filter ->
				filter == HttpLimitFilter.class
						|| filter == HttpEchoFilter.class
						|| filter == HttpRetryFilter.class
						|| filter == HttpValidationFilter.class
			);

			this.fs.add(0, HttpLimitFilter.class);
			this.fs.add(0, HttpValidationFilter.class);
			this.fs.add(HttpRetryFilter.class);
			this.fs.add(HttpEchoFilter.class);
			List<Filter> filters = this.fs.stream().distinct().map(f -> KM.newInstance(f)).collect(Collectors.toList());
			this.e.chanInvoker = this.e.buildInvokerChain(this.e, filters);
			return this.e;
		}
	}





}
