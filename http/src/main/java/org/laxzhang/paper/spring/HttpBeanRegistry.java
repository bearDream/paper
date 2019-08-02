package org.laxzhang.paper.spring;

import org.laxzhang.paper.PaperClient;
import org.laxzhang.paper.proxy.HttpProxy;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 14:42
 */
@Slf4j
public enum  HttpBeanRegistry {
	/**
	 * get Http bean registry. Just see code.
	 */
	get;
	private ConfigurableListableBeanFactory beanFactory;
	private boolean isDefaultListableBeanFactory;

	void setBeanFactory(ConfigurableListableBeanFactory beanFactory){
		this.beanFactory = beanFactory;
		this.isDefaultListableBeanFactory = beanFactory instanceof DefaultListableBeanFactory;
	}

	void registerBeanDefinition(Class<?> type){
		if (!type.isAnnotationPresent(PaperClient.class)){
			return;
		}
		log.info("Http interface is found ==>> " + type.getName());
		Object bean = HttpProxy.create().proxy(type);
		beanFactory.registerResolvableDependency(type, bean);
		beanFactory.registerSingleton(formaterBeanName(type), bean);

	}

	/**
	 * set PaperHttp Client bean name.
	 * @param type
	 * @return
	 */
	private String formaterBeanName(Class<?> type){
		String name = type.getSimpleName();
		if (name.length() > 2 && Character.isUpperCase(name.charAt(0)) && Character.isUpperCase(name.charAt(1))){
			return Character.toLowerCase(name.charAt(1)) + name.substring(2);
		}
		return Character.toLowerCase(name.charAt(0)) + name.substring(1);
	}
}
