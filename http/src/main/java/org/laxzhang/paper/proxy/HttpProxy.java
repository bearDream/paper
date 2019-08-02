package org.laxzhang.paper.proxy;

import org.laxzhang.common.KM;
import org.laxzhang.paper.PaperClient;
import org.laxzhang.paper.client.OKHttpInvoker;
import org.laxzhang.paper.invoker.HttpInvoker;
import org.laxzhang.paper.invoker.Invoker;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 14:50
 */
public class HttpProxy implements Proxy {

	private Invoker<?> invoker;

	private HttpProxy(Class<? extends Invoker<?>> invoker){
		this.invoker = KM.newInstance(invoker);
	}

	public static Proxy create(){
		return new HttpProxy(OKHttpInvoker.class);
	}

	public static Proxy create(Class<? extends Invoker<?>> invoker){
		return new HttpProxy(invoker);
	}

	@Override
	public <SERVICE> SERVICE target() {
		return (SERVICE) this.invoker;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <SERVICE> SERVICE proxy(Class<SERVICE> service) {

		PaperClient client = service.getAnnotation(PaperClient.class);
		if (null == client){
			throw new UnsupportedOperationException("Interface must annotated by @PaperClient.");
		}

		return (SERVICE) java.lang.reflect.Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{ service }, new HttpInvoker(client, invoker));
	}
}
