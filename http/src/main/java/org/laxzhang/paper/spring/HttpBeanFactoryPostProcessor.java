package org.laxzhang.paper.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

/**
 * 用以在所有Bean被加载进来，但还没有被初始化之前，获取BeanFactory, 用以注册Http接口使用。
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 11:23
 */
public class HttpBeanFactoryPostProcessor implements BeanFactoryPostProcessor, Ordered {


	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
		HttpBeanRegistry.get.setBeanFactory(configurableListableBeanFactory);
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
