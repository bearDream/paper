package org.laxzhang.paper.spring;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.laxzhang.common.KM;
import org.laxzhang.common.ifake.IError;
import org.laxzhang.paper.exceptions.PaperClientException;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Spring Application utils.
 * Get bean.
 * @see ApplicationContextListener
 * @author laxzhang@outlook.com
 * @Date 2019/4/24 14:58
 */
public class SpringContext {

	private static ApplicationContext context;
	private static ConfigurableEnvironment env;

	public static <T> T getBean(String name){
		return (T) context.getBean(name);
	}

	public static <T> T getBean(Class<?> clazz){
		return (T) context.getBean(clazz);
	}

	/**
	 * Get bean, if clazz is not a bean, then use default class.
	 * @param clazz
	 * @param defaultClazz
	 * @param <T>
	 * @return
	 */
	public static <T> T getBeanIf(Class<T> clazz, T defaultClazz){
		return KM.catchThen(() -> context.getBean(clazz), () -> defaultClazz);
	}

	public static String getProp(String key, String defaultValue){
		return null == env ? defaultValue : env.getProperty(key, defaultValue);
	}

	public static String getProp(String key){
		return getProp(key, "");
	}

	/**
	 * Auto set value to fillClass
	 * @param key
	 * @param fillClass
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> getProp(String key, Class<T> fillClass){
		if (env == null){
			return new ArrayList<>(0);
		}
		List<T> props = new ArrayList<>();
		// Get all field of fillClass, no matter modifier what is it.
		Field[] fields = fillClass.getDeclaredFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				int index = 0;
				while (true){
					String name = (key + "[" + index + "]." + field.getName()).intern();
					String value = env.getProperty(name);
					if (null == value){
						break;
					}
					if (props.size() < index+1){
						props.add(fillClass.newInstance());
					}
					T ins = props.get(index);
					index++;
					field.set(ins, KM.cast(value, field.getType()));
				}
			}
		}catch (Exception e){
			throw new PaperClientException(IError.SYSTEM, e);
		}
		return props;
	}


	public static ApplicationContext getContext() {
		return context;
	}

	public static ConfigurableEnvironment getEnv() {
		return env;
	}

	public static void setContext(ApplicationContext context) {
		SpringContext.context = context;
	}

	public static void setEnv(ConfigurableEnvironment env) {
		SpringContext.env = env;
	}
}
