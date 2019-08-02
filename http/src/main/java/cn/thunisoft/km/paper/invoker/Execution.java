package cn.thunisoft.km.paper.invoker;

import cn.thunisoft.km.paper.handler.CauseHandler;
import cn.thunisoft.km.paper.handler.ParamSerializer;
import cn.thunisoft.km.paper.handler.ResultHandler;

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
