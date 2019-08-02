package cn.thunisoft.km.paper.proxy;

/**
 * Proxy interface. CGlib or Jdk proxy must implements this interface.
 * @author laxzhang@outlook.com
 * @Date 2019/4/19 16:43
 */
public interface Proxy {

	<SERVICE> SERVICE target();

	<SERVICE> SERVICE proxy(Class<SERVICE> services);
}
