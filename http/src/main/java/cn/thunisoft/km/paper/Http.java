package cn.thunisoft.km.paper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import cn.thunisoft.km.paper.bridge.RequestBody;
import cn.thunisoft.km.paper.invoker.Execution;
import cn.thunisoft.km.paper.invoker.HttpInvocation;

/**
 * Request interface by Get method.
 * @author laxzhang@outlook.com
 * @Date 2019/4/17 14:51
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Http {

	/**
	 * alias for {@link #path()}.
	 * @return
	 */
	String value() default "";

	/**
	 * Request child path. relative server address.
	 * @return
	 */
	String path() default "";

	/**
	 * limit per second. ignore that rather than max integer.
	 */
	int limit() default Integer.MAX_VALUE;

	/**
	 * fault delay millions. ignore that rather than 20000.
	 */
	int delay() default 2000;

	/**
	 * fault retry times
	 */
	int retries() default 5;

	/**
	 * http getMethod
	 */
	Method method() default Method.GET;

	/**
	 * request content
	 */
	Content request() default Content.JSON;

	/**
	 * response content
	 */
	Content response() default Content.JSON;

	/**
	 * redirect request
	 */
	boolean redirect() default true;

	/**
	 * encode all of the url parameters
	 */
	boolean urlencode() default false;

	/**
	 * {@link #MERGE} deconstruct all parameter object to a map
	 * 				  If you use multi params as method args, use this annotation.
	 * {@link #WRAP} otherwise wrap all parameters in a map object
	 * 				 If you want to post a list or one object, try this annotation.
	 * {@link #VALUE} only use the first valid value without key.
	 * @see HttpInvocation#HttpInvocation(Execution, java.lang.reflect.Method, Object[])
	 * @see RequestBody#serialize()
	 */
	int body() default WRAP;

	int MERGE = -1;
	int WRAP = 0;
	int VALUE = 1;

	enum Method {
		/**
		 * Http Method enums
		 */
		GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE;

		public boolean oneOf(Http.Method... methods) {
			if (null == methods || methods.length < 1) {
				return false;
			}
			for (Http.Method method : methods) {
				if (this == method) {
					return true;
				}
			}
			return false;
		}
	}

	enum Content {
		/**
		 * Http response enums.
		 * Notice: Do Not recommend JAVA Content. instead of JSON.
		 */
		JSON, FORM, XML, TEXT, STREAM, JAVA;

		public boolean oneOf(Http.Content... contents) {
			if (null == contents || contents.length < 1) {
				return false;
			}
			for (Http.Content content : contents) {
				if (this == content) {
					return true;
				}
			}
			return false;
		}
	}
}
