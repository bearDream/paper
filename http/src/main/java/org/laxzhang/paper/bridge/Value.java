package org.laxzhang.paper.bridge;

import org.laxzhang.paper.Http;
import org.laxzhang.paper.handler.ParamSerializer;

/**
 * Request value wrap class.
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 15:54
 */
public class Value implements IValue {

	private Http http;
	private ParamSerializer serializer;
	private Object value;

	public Value(Http http, ParamSerializer serializer, Object value){
		this.http = http;
		this.serializer = serializer;
		this.value = value;
	}

	@Override
	public Object get() {
		return this.value;
	}

	@Override
	public String serialize() {
		return this.serializer.serialize(http, this.value);
	}
}
