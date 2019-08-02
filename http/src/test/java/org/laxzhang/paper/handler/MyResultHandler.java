package org.laxzhang.paper.handler;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.laxzhang.common.KM;
import org.laxzhang.paper.Http;
import org.laxzhang.paper.exceptions.PaperClientException;
import org.laxzhang.paper.invoker.Invocation;
import org.laxzhang.paper.support.XML;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/28 10:09
 */
public class MyResultHandler implements ResultHandler {

	@Override
	@SuppressWarnings("unchecked")
	public Object handler(Invocation invocation, byte[] response) throws Throwable {
		Type returnType = invocation.returnType();
		Http http = invocation.annotationOf(Http.class, Invocation.METHOD);
		if (http.response() == Http.Content.JSON){
			JSONObject jo = JSON.parseObject(response, JSONObject.class);
			if (null == jo){
				throw new PaperClientException("-{}--{}", invocation.url(), "No Response..");
			}
			String error = jo.getString("error");
			if (!KM.isBlank(error)){
				throw new PaperClientException(error);
			}
			try {
				Object res = parseJson(response, returnType);
				return res;
			}catch (Exception e){
				return KM.newInstance(Class.forName(returnType.getTypeName()));
			}

		}else if (http.response() == Http.Content.XML){
			return XML.unmarshal(response, (Class<?>) returnType);
		}else if (http.response() == Http.Content.TEXT){
			return new String(response, Charset.defaultCharset());
		}else {
			return response;
		}
	}
}
