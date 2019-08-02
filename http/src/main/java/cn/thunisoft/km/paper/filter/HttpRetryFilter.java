package cn.thunisoft.km.paper.filter;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import cn.thunisoft.km.common.KM;
import cn.thunisoft.km.paper.Http;
import cn.thunisoft.km.paper.client.OKHttpClient;
import cn.thunisoft.km.paper.invoker.Invocation;
import cn.thunisoft.km.paper.invoker.Invoker;

/**
 * Retry Filter.
 * @author laxzhang@outlook.com
 * @Date 2019/4/23 11:47
 */
public class HttpRetryFilter implements Filter {


	@Override
	public Object invoke(Invoker<?> invoker, Invocation invocation) throws Throwable {
		Http http = invocation.annotationOf(Http.class, Invocation.METHOD);
		int retries = http.retries();
		for (int i = 0; i < retries - 1; i++) {
			try {
				return invoker.invoker(invocation);
			}catch (Exception e){
				if (!canRetry(e)){
					throw e;
				}

				if (shouldDelay(e) && http.delay() < 20000){
					Thread.sleep(http.delay());
				}
			}
		}
		return invoker.invoker(invocation);
	}

	private boolean shouldDelay(Throwable t){
		boolean delay = KM.isBelongToThrowable(t, SocketException.class);
		delay = delay || KM.isBelongToThrowable(t, SocketTimeoutException.class);
		delay = delay || KM.isBelongToThrowable(t, IOException.class);
		return delay;
	}

	private boolean canRetry(Throwable t){
		boolean canRetry = KM.isBelongToThrowable(t, SocketTimeoutException.class);
		canRetry = canRetry || KM.isBelongToThrowable(t, SocketException.class);
		canRetry = canRetry || KM.isBelongToThrowable(t, ConnectException.class);
		canRetry = canRetry || KM.isBelongToThrowable(t, IOException.class);
		return canRetry;
	}


}
