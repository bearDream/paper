package org.laxzhang.paper.invoker;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import org.laxzhang.paper.bridge.IRequestBody;
import org.laxzhang.paper.bridge.IRequestQuery;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 15:44
 */
public interface Invocation extends Serializable {

	Invoker<?> getProxy();

	Class<?> getInterface();

	Method getMethod();

	Object[] getParameters();

	Type getType();

	/**
	 * Get Interface method annotation.
	 * @param a
	 * @param level
	 * @param <T>
	 * @return
	 */
	<T extends Annotation> T annotationOf(Class<T> a, int level);

	/**
	 * request url
	 * @return
	 */
	IRequestQuery url();

	/**
	 * add query params
	 * @param key
	 * @param value
	 * @return
	 */
	IRequestQuery addQuery(String key, Object value);

	/**
	 * requst body params.
	 * @return
	 */
	IRequestBody addArgs(String key, Object value);

	/**
	 * request body payload.
	 * @return
	 */
	IRequestBody getArgs();

	/**
	 * request header.
	 * @return
	 */
	Map<String, String> getHeaders();

	/**
	 * return type
	 * @return
	 */
	Type returnType();

	/**
	 * add requst header.
	 * @param key
	 * @param value
	 * @return
	 */
	Map<String, String> addHeaders(String key, String value);

	int TYPE = 0;
	int METHOD = 1;
}
