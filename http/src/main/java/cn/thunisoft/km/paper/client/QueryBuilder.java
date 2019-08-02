package cn.thunisoft.km.paper.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.thunisoft.km.common.KM;
import cn.thunisoft.km.paper.Http;
import cn.thunisoft.km.paper.invoker.Invocation;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Builder query parameters, form.
 * @author laxzhang@outlook.com
 * @Date 2019/4/25 15:56
 */
public final class QueryBuilder {

	private final static List<Headers> DEFAULT_HEADER = new ArrayList<>();

	static {
		DEFAULT_HEADER.add(new Headers.Builder().add("Accept-Language", "en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4").build());
		DEFAULT_HEADER.add(new Headers.Builder().add("Cache-Control", "max-age=0").build());
		DEFAULT_HEADER.add(new Headers.Builder().add("Connection", "keep-alive").build());
		DEFAULT_HEADER.add(new Headers.Builder().add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36").build());
	}

	public static Headers buildHeaders(Invocation invocation){
		Map<String, String> headerMap = invocation.getHeaders();
		List<Headers> headersList = buildHeaderList(headerMap);
		headersList.addAll(DEFAULT_HEADER);

		Http http = invocation.annotationOf(Http.class, Invocation.METHOD);
		if (http.request().oneOf(Http.Content.JSON)){
			headersList.add(new Headers.Builder().add(HttpConsts.CONTENT_TYPE, HttpConsts.ContentType.JSON_TYPE.toString()).build());
		}
		if (http.request().oneOf(Http.Content.XML)){
			headersList.add(new Headers.Builder().add(HttpConsts.CONTENT_TYPE, HttpConsts.ContentType.XML_TYPE.toString()).build());
		}
		if (http.request().oneOf(Http.Content.FORM)){
			headersList.add(new Headers.Builder().add(HttpConsts.CONTENT_TYPE, HttpConsts.ContentType.FORM_TYPE.toString()).build());
		}
		if (http.request().oneOf(Http.Content.JAVA)){
			headersList.add(new Headers.Builder().add(HttpConsts.CONTENT_TYPE, HttpConsts.ContentType.JAVA_TYPE.toString()).build());
		}
		if (http.request().oneOf(Http.Content.STREAM, Http.Content.TEXT)){
			headersList.add(new Headers.Builder().add(HttpConsts.CONTENT_TYPE, HttpConsts.ContentType.MUL_FORM_TYPE.toString()).build());
		}
		Headers.Builder builder = new Headers.Builder();
		headersList.forEach(h -> builder.addAll(h));
		return builder.build();
	}

	public static RequestBody buildForm(Invocation invocation){
		Http http = invocation.annotationOf(Http.class, Invocation.METHOD);
		if (http.request().oneOf(Http.Content.JSON)){
			return RequestBody.create(MediaType.parse(HttpConsts.ContentType.JSON_TYPE.type), invocation.getArgs().serialize());
		}
		if (http.request().oneOf(Http.Content.XML)){
			return RequestBody.create(MediaType.parse(HttpConsts.ContentType.XML_TYPE.type), invocation.getArgs().serialize());
		}
		if (http.request().oneOf(Http.Content.JAVA)){
			return RequestBody.create(MediaType.parse(HttpConsts.ContentType.JAVA_TYPE.type), invocation.getArgs().serialize());
		}
		if (http.request().oneOf(Http.Content.FORM)){
			List<FormBody> formList = buildFormList(invocation);
			FormBody.Builder formBody = new FormBody.Builder();
			for (int i = 0; i < formList.size(); i++) {
				formBody.add(formList.get(i).name(i), formList.get(i).value(i));
			}
			return formBody.build();
		}
		if (http.request().oneOf(Http.Content.STREAM, Http.Content.TEXT)){

			MultipartBody.Builder builder = new MultipartBody.Builder();

			invocation.getArgs().entrySet().forEach(kv -> {
				if (kv.getValue().get().getClass() == byte[].class){
					String fileName = KM.randomNumeric(10);
					RequestBody fileBody = MultipartBody.create(MediaType.parse("image/png"), (byte[]) kv.getValue().get());
					builder.addFormDataPart(fileName, fileName + ".jpg", fileBody);
				}else {
					builder.addFormDataPart(kv.getKey(), (String) kv.getValue().get());
				}
			});
			return builder.setType(MediaType.parse(HttpConsts.ContentType.MUL_FORM_TYPE.type)).build();
		}
		return RequestBody.create(MediaType.parse(HttpConsts.ContentType.JSON_TYPE.type), invocation.getArgs().serialize());
	}



	/**
	 * flip headers of map to list.
	 * @param headers
	 * @return
	 */
	public static List<Headers> buildHeaderList(Map<String, String> headers){
		return headers.entrySet().stream().map(kv -> new Headers.Builder().add(kv.getKey(), kv.getValue()).build()).collect(Collectors.toList());
	}

	/**
	 * build form according to invocation
	 * @param invocation
	 * @return
	 */
	public static List<FormBody> buildFormList(Invocation invocation){
		return invocation.getArgs().entrySet().stream().map(kv -> new FormBody.Builder().add(kv.getKey(), kv.getValue().serialize()).build()).collect(Collectors.toList());
	}

}
