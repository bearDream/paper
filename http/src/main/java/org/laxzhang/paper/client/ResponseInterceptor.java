package org.laxzhang.paper.client;

import java.io.IOException;

import org.laxzhang.paper.exceptions.PaperClientException;
import org.laxzhang.paper.exceptions.RedirectException;
import org.laxzhang.paper.exceptions.RetryEception;
import org.laxzhang.paper.filter.HttpRetryFilter;
import org.laxzhang.paper.support.HttpCode;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Process http response data.
 * Achieve Http retry method.
 *
 * @see HttpRetryFilter
 * @author laxzhang@outlook.com
 * @Date 2019/4/26 17:51
 */
public class ResponseInterceptor implements Interceptor {

	final static Logger logger = LoggerFactory.getLogger(ResponseInterceptor.class);

	@Override
	public Response intercept(Chain chain) throws IOException {
		Response response = chain.proceed(chain.request());
		int status = response.code();
		String url = chain.request().url().toString();

		logger.info("code --{}-- message --{}-- protocol --{}-- ", response.code(), response.message(), response.protocol());
		if (status >= HttpCode.OK && status < HttpCode.MULTIPLE_CHOICE){
			return response;
		}

		if (status >= HttpCode.INTERNEL_ERROR){
			throw new RetryEception("url:--{}--status--{}", url, status);
		}
		if (status == HttpCode.REDIRECT){
			throw new RedirectException("url:--{}--status--{}", url, status);
		}

		throw new PaperClientException("url:--{}--status--{}", url, status);
	}
}
