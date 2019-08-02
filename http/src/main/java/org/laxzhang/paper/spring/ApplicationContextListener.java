package org.laxzhang.paper.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 应用启动前将context和environment设置进来，在后面的参数注入以及Spring的工具中需要用到。
 * @see SpringContext
 * @author laxzhang@outlook.com
 * @Date 2019/4/24 15:46
 */
public final class ApplicationContextListener implements ApplicationContextAware {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContext.setContext(applicationContext);
		ConfigurableApplicationContext configurable = (ConfigurableApplicationContext) applicationContext;
		ConfigurableEnvironment environment = configurable.getEnvironment();
		SpringContext.setEnv(environment);
	}
}
