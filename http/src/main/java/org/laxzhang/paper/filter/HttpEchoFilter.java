package org.laxzhang.paper.filter;

import org.laxzhang.common.KM;
import org.laxzhang.paper.invoker.Invocation;
import org.laxzhang.paper.invoker.Invoker;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/23 11:25
 */
public class HttpEchoFilter implements Filter {

	final static Logger logger = LoggerFactory.getLogger(HttpEchoFilter.class);

	@Override
	public Object invoke(Invoker<?> invoker, Invocation invocation) throws Throwable {

		try {
			String requestId = KM.randomNumeric(5);
			String url = invocation.url().toString();
			String args = invocation.getArgs().serialize();
			String head = JSON.toJSONString(invocation.getHeaders());
			logger.info("Invoker class:--{}--{}--Id:--{}--url:--{}--args:--{}--head:--{}", invocation.getInterface().getName(), invocation.getMethod().getName(), requestId, url, args, head);
			Object result = invoker.invoker(invocation);
			logger.info("Id:--{}--url:--{}--result: {}", requestId, url, JSON.toJSONString(result));

			return result;
		}catch (Exception e){
			logger.error(invocation.getInterface().getName(), e);
			throw e;
		}
	}
}
