package org.laxzhang.paper.client;

import org.laxzhang.paper.invoker.Invocation;
import org.laxzhang.paper.invoker.Invoker;

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
