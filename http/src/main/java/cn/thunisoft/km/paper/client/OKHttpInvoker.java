package cn.thunisoft.km.paper.client;

import cn.thunisoft.km.paper.invoker.Invocation;
import cn.thunisoft.km.paper.invoker.Invoker;

/**
 * OkHttpClient invoker.
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 16:28
 */
public class OKHttpInvoker implements Invoker<byte[]> {
	@Override
	public byte[] invoker(Invocation invocation) throws Throwable {
		return OKHttpClient.instance.execute(invocation);
	}
}
