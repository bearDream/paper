package org.laxzhang.paper.invoker;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 15:43
 */
public interface Invoker<T> {

	T invoker(Invocation invocation) throws Throwable;
}
