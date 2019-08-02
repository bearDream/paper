package cn.thunisoft.km.paper.handler;

import cn.thunisoft.km.paper.invoker.Invocation;

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
