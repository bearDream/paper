package org.laxzhang.paper.handler;

import java.lang.reflect.Type;

import org.laxzhang.paper.invoker.Invocation;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 18:10
 */
public interface ResultHandler {

	Logger logger = LoggerFactory.getLogger(ResultHandler.class);

	Object handler(Invocation invocation, byte[] response) throws Throwable;

	default Object handlerPrint(Invocation invocation, byte[] response) throws Throwable{
		return handler(invocation, response);
	}

	default Object parseJson(byte[] response, Type returnType) throws Exception{
		try {
			StringBuffer dealStr = new StringBuffer(new String(response));
			logger.info("json: --{}--", dealStr);
			Object result = JSON.parseObject(response, returnType);
			return result;
		}catch (Exception e){
			logger.warn("Parse Result occurred error. --{}--", e.getMessage());
			throw e;
		}
	}
}
