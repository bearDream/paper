package cn.thunisoft.km.paper.handler;

import cn.thunisoft.km.paper.exceptions.RedirectException;
import cn.thunisoft.km.paper.invoker.Invocation;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/23 10:30
 */
public interface CauseHandler {

	Object handler(Invocation invocation, Throwable t) throws Throwable;

	/**
	 * Default handler redirect exception.
	 * @TODO
	 * @param invocation
	 * @param e
	 * @return
	 */
	default Object handlerRedirect(Invocation invocation, RedirectException e){
		return null;
	}
}
