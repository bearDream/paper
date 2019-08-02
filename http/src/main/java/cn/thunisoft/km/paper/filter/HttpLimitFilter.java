package cn.thunisoft.km.paper.filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import cn.thunisoft.km.paper.Http;
import cn.thunisoft.km.paper.invoker.Invocation;
import cn.thunisoft.km.paper.invoker.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/23 11:37
 */
public class HttpLimitFilter implements Filter {

	final static Logger logger = LoggerFactory.getLogger(HttpLimitFilter.class);
	private static final Map<Http, Semaphore> SEMAPHORES = new ConcurrentHashMap<>();
	private int MAX_LIMIT = 1000;

	@Override
	public Object invoke(Invoker<?> invoker, Invocation invocation) throws Throwable {
		Http http = invocation.annotationOf(Http.class, Invocation.METHOD);
		if (http.limit() >= MAX_LIMIT){
			logger.warn("No limit for http invoker.");
			return invoker.invoker(invocation);
		}
		SEMAPHORES.putIfAbsent(http, new Semaphore(http.limit()));
		try {
			SEMAPHORES.get(http).acquire();
			return invoker.invoker(invocation);
		}finally {
			SEMAPHORES.get(http).release();
		}
	}
}
