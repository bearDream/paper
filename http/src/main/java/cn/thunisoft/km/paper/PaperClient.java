package cn.thunisoft.km.paper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.thunisoft.km.paper.filter.Filter;
import cn.thunisoft.km.paper.handler.CauseHandler;
import cn.thunisoft.km.paper.handler.DefaultCauseHandler;
import cn.thunisoft.km.paper.handler.DefaultParamSerialize;
import cn.thunisoft.km.paper.handler.DefaultResultHandler;
import cn.thunisoft.km.paper.handler.ParamSerializer;
import cn.thunisoft.km.paper.handler.ResultHandler;
import cn.thunisoft.km.paper.handler.RootX;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PaperClient {

	/**
	 * @see {@link #style()}
	 */
	int NONE = -1;
	int HUMP = 0;
	int LINE = 1;

	/**
	 * Request Params style.
	 * For example: BeautifulGirl is hump style {@link #HUMP}, beautiful_girl is underline style {@link #LINE}
	 * @return
	 */
	int style() default NONE;

	/**
	 * request root path.
	 * <P>For example: If you want to invoker WeChat interface, like this: https://api.weixin.qq.com/cgi-bin/menu/create, root is https://api.weixin.qq.com/cgi-bin/</P>
	 * @return request path.
	 */
	String root() default "";

	/**
	 * alias for the {@link #root()}.
	 * @return
	 */
	String value() default "";

	/**
	 * For root enhance. Suitable for environmental switching.
	 * <P>once the environment is https://api.weixin.qq.com/cgi-bin</P>
	 * <P>next the environment is change https://api.weixin.qq.com/v2</P>
	 * By use rootX to select environment.
	 * @return
	 */
	Class<? extends RootX>[] rootX() default {};

	/**
	 * exception handler.
	 * @return
	 */
	Class<? extends CauseHandler>[] cause() default DefaultCauseHandler.class;

	/**
	 * binary result handler
	 */
	Class<? extends ResultHandler>[] result() default DefaultResultHandler.class;

	/**
	 * request filters
	 */
	Class<? extends Filter>[] filter() default {};

	/**
	 * bean serialize.
	 * @return
	 */
	Class<? extends ParamSerializer>[] serializer() default DefaultParamSerialize.class;

	/**
	 * connect to remote server time out
	 * mill seconds
	 */
	int connectTimeout() default 15000;

	/**
	 * read response time out
	 * mill seconds
	 */
	int readTimeout() default 20000;
}
