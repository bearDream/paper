package org.laxzhang.paper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * Enable Paper Http can create Http Rest client.
 * @author laxzhang@outlook.com
 * @Date 2019.4.17
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(PaperHttpConfig.class)
public @interface EnablePaperHttp {

	/**
	 * alias for the {@link #basePackage()}. Same as {@link #basePackage()}.
	 * @return
	 */
	String[] value() default "";

	/**
	 * @return scan path for autowired.
	 */
	String[] basePackage() default "";
}
