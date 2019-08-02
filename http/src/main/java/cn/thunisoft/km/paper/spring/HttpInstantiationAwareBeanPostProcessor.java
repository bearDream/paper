package cn.thunisoft.km.paper.spring;

import java.lang.reflect.Field;
import java.util.Arrays;

import cn.thunisoft.km.common.KM;
import cn.thunisoft.km.paper.PaperClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.Ordered;

/**
 * Just find Http interface with {@link PaperClient}, then proxy it, finally register as a bean.
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 11:22
 */
public class HttpInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements Ordered {


	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		scanPapaerClientInterfaces(beanClass);
		return super.postProcessBeforeInstantiation(beanClass, beanName);
	}

	/**
	 * find {@link PaperClient} annotated interface by greed scan.
	 * @param beanClass
	 */
	private void scanPapaerClientInterfaces(Class<?> beanClass){
		if (!KM.greedScans(beanClass)){
			return;
		}

		Field[] fields = beanClass.getDeclaredFields();
		if (!KM.isEmpty(fields)){
			Arrays.stream(fields).forEach(field -> {
				HttpBeanRegistry.get.registerBeanDefinition(field.getType());
			});
		}
		scanPapaerClientInterfaces(beanClass.getSuperclass());
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
