package cn.thunisoft.km.paper;

import cn.thunisoft.km.paper.spring.ApplicationContextListener;
import cn.thunisoft.km.paper.spring.HttpBeanFactoryPostProcessor;
import cn.thunisoft.km.paper.spring.HttpInstantiationAwareBeanPostProcessor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 11:20
 */
@Configuration
public class PaperHttpConfig {

	@Bean
	public HttpInstantiationAwareBeanPostProcessor httpInstantiationAwareBeanPostProcessor(){
		return new HttpInstantiationAwareBeanPostProcessor();
	}

	@Bean
	public HttpBeanFactoryPostProcessor httpBeanFactoryPostProcessor(){
		return new HttpBeanFactoryPostProcessor();
	}

	@Bean
	public ApplicationContextListener contextListener(){
		return new ApplicationContextListener();
	}

}
