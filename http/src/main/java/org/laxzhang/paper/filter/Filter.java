package org.laxzhang.paper.filter;

import org.laxzhang.paper.invoker.Invocation;
import org.laxzhang.paper.invoker.Invoker;

/**
 * filter for invoker execute.
 *
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 18:09
 */
public interface Filter {
	Object invoke(Invoker<?> invoker, Invocation invocation) throws Throwable;
}
