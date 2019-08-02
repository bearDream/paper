package cn.thunisoft.km.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.thunisoft.km.common.ifake.IConvert;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/25 9:54
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Format {

	/**
	 * alias for format name.
	 * @return
	 */
	String value() default "";

	/**
	 * format text
	 * @return
	 */
	String format() default "";

	/**
	 * convert util.
	 * @return
	 */
	Class<? extends IConvert<?, ?>> convert() default IConvert.DefaultConvert.class;
}
