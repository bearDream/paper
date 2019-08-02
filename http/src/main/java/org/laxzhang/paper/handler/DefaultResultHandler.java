package org.laxzhang.paper.handler;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.laxzhang.paper.Http;
import org.laxzhang.paper.invoker.Invocation;
import org.laxzhang.paper.support.XML;

/**
 * Process http return data.
 * Type is Json, use fastJson process.
 * Type is XML, use javax.xml to process.
 * Type is TEXT, return new String.
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 18:16
 */
public class DefaultResultHandler implements ResultHandler {

	@Override
	public Object handler(Invocation invocation, byte[] response) throws Throwable {

		Http http = invocation.annotationOf(Http.class, Invocation.METHOD);
		Type returnType = invocation.returnType();
		if (http.response() == Http.Content.JSON){
			return parseJson(response, returnType);
		}else if (http.response() == Http.Content.XML){
			return XML.unmarshal(response, (Class<?>) returnType);
		}else if (http.response() == Http.Content.TEXT){
			return new String(response, Charset.defaultCharset());
		}else {
			return response;
		}
	}
}
