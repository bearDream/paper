package org.laxzhang.paper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Cause Java compile code will drop parameter name, so use this annotation can tell Paper httpclient your params name.
 * @author laxzhang@outlook.com
 * @Date 2019/4/17 13:51
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathVariable {

	String value() default "";
}
