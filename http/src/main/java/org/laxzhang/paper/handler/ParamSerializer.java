package org.laxzhang.paper.handler;

import org.laxzhang.paper.Http;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 16:40
 */
public interface ParamSerializer {

	String serialize(Http http, Object o);
}
