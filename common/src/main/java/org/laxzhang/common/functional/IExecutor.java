package org.laxzhang.common.functional;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 16:02
 */
@FunctionalInterface
public interface IExecutor<T> {
	T execute() throws Throwable;
}
