package org.laxzhang.paper;

import org.laxzhang.paper.beans.Packet;
import org.laxzhang.paper.iface.IPacket;
import org.laxzhang.paper.proxy.HttpProxy;
import org.junit.Test;


/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/28 11:28
 */
public class HttpTest {

	static IPacket iPacket = HttpProxy.create().proxy(IPacket.class);

	@Test
	public void testGet(){
		Packet packet = iPacket.hello("zhongtong", "77191000123463");
		System.out.println(packet.toString());
	}

	@Test
	public void testPost(){
		Packet packet = iPacket.hello("zhongtong", "77191000123463");
		System.out.println(packet.toString());
	}
}
