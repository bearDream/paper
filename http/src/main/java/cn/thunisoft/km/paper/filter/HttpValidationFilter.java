package cn.thunisoft.km.paper.filter;

import cn.thunisoft.km.paper.invoker.Invocation;
import cn.thunisoft.km.paper.invoker.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @TODO
 * @author laxzhang@outlook.com
 * @Date 2019/4/23 15:33
 */
public class HttpValidationFilter implements Filter {

	final static Logger log = LoggerFactory.getLogger(HttpValidationFilter.class);

	@Override
	public Object invoke(Invoker<?> invoker, Invocation invocation) throws Throwable {
		log.info("Validation Filter wait implement...");
		return invoker.invoker(invocation);
	}
}
