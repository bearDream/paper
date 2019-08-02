package cn.thunisoft.km.paper.filter;

import cn.thunisoft.km.paper.invoker.Invocation;
import cn.thunisoft.km.paper.invoker.Invoker;

/**
 * filter for invoker execute.
 *
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 18:09
 */
public interface Filter {
	Object invoke(Invoker<?> invoker, Invocation invocation) throws Throwable;
}
