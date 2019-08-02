package org.laxzhang.paper.handler;

import org.laxzhang.paper.invoker.Invocation;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/23 10:58
 */
public class DefaultCauseHandler implements CauseHandler {
	@Override
	public Object handler(Invocation invocation, Throwable t) throws Throwable {
		throw t;
	}
}
