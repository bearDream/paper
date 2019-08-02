package cn.thunisoft.km.paper;

import cn.thunisoft.km.common.KM;
import cn.thunisoft.km.paper.beans.Packet;
import cn.thunisoft.km.paper.iface.IPacket;
import cn.thunisoft.km.paper.proxy.HttpProxy;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


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
