package org.laxzhang.paper.invoker;

import org.laxzhang.paper.handler.CauseHandler;
import org.laxzhang.paper.handler.ParamSerializer;
import org.laxzhang.paper.handler.ResultHandler;

/**
 * Http Execution. If you want to use different http client, just implement this interface.
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 16:40
 */
public interface Execution<O> {

	O execute(Invocation invocation) throws Throwable;

	Invoker<?> getInvoker();

	String getServer();

	ParamSerializer getHttpSerializer();

	CauseHandler getCauseHandler();

	ResultHandler getResultHandler();
}
