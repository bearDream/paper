package org.laxzhang.paper.client;

import java.time.Duration;
import java.util.function.Supplier;

import org.laxzhang.common.KM;
import org.laxzhang.paper.Http;
import org.laxzhang.paper.PaperClient;
import org.laxzhang.paper.exceptions.PaperClientException;
import org.laxzhang.paper.invoker.Invocation;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 16:27
 */
public enum OKHttpClient {

	/**
	 * initial OkHttpClient.
	 */
	instance;

	private final static Logger logger = LoggerFactory.getLogger(OKHttpClient.class);

	private void checkUrl(Invocation invocation){
		if (KM.isBlank(invocation.url().toString())){
			throw new PaperClientException("{}--url can not be null.", invocation.getInterface().getName());
		}
	}

	public byte[] execute(Invocation invocation) throws Throwable{
		Http http = invocation.annotationOf(Http.class, Invocation.METHOD);
		checkUrl(invocation);

		return load(() -> {
			Request.Builder request = new Request.Builder();
			request.headers(QueryBuilder.buildHeaders(invocation));
			request.url(HttpUrl.get(invocation.url().toString()));
			if (http.method().oneOf(Http.Method.POST)){
				request.post(QueryBuilder.buildForm(invocation));
			}
			if (http.method().oneOf(Http.Method.PUT)){
				request.put(QueryBuilder.buildForm(invocation));
			}
			return request.build();
		}, invocation);
	}

	private OkHttpClient createClient(Invocation invocation) {
		PaperClient paperClient = invocation.annotationOf(PaperClient.class, Invocation.TYPE);
		// config connection pool.
//		ConnectionPool pool = new ConnectionPool(5, 60, TimeUnit.SECONDS);
		return new OkHttpClient.Builder()
				.connectTimeout(Duration.ofMillis(paperClient.connectTimeout()))
				.readTimeout(Duration.ofMillis(paperClient.readTimeout()))
				.addInterceptor(new ResponseInterceptor())
				.build();
	}

	public byte[] load(Supplier<Request> supplier, Invocation invocation) throws Throwable{
		OkHttpClient client = createClient(invocation);
		Request request = supplier.get();

		logger.info("request info ==>>> {}", request.toString());
		Response response = client.newCall(request).execute();
		return response.body().bytes();
	}
}
