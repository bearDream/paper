package org.laxzhang.paper.bridge;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 15:48
 */
public interface IRequestQuery {

	IRequestQuery clone();

	IRequestQuery addQuery(String key, Object value);

	String toString();
}
