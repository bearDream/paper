package org.laxzhang.paper.bridge;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.laxzhang.common.KM;
import org.laxzhang.paper.Http;
import org.laxzhang.paper.PaperClient;
import org.laxzhang.paper.handler.ParamSerializer;
import org.laxzhang.paper.support.Cache;

/**
 * RequstBody entity.
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 15:54
 */
public class RequestBody implements IRequestBody {

	private PaperClient client;
	private Http http;
	private ParamSerializer serializer;
	private Map<String, Object> arguments;

	public RequestBody(PaperClient client, Http http, ParamSerializer serializer, Map<String, Object> args){
		this.client = client;
		this.http = http;
		this.serializer = serializer;
		this.arguments = args;
	}

	@Override
	public String serialize() {
		boolean onlyVal = http.body() == Http.VALUE | http.request().oneOf(Http.Content.XML);
		if (onlyVal){
			return this.serializer.serialize(http, arguments.values().stream().findFirst().orElse(null));
		}
		return this.serializer.serialize(http, arguments);
	}

	@Override
	public IRequestBody put(String key, Object value) {
		this.arguments.put(key, value);
		return this;
	}

	/**
 	 * @param key
	 * @return
	 */
	@Override
	public IValue get(String key) {
		Object val = arguments.get(key);
		if (null == val){
			return new Value(http, serializer, val);
		}
		val = arguments.get(KM.humpToLine(key));
		if (val != null){
			return new Value(http, serializer, val);
		}
		val = arguments.get(KM.lineToHump(key));
		return new Value(http, serializer, val);
	}

	@Override
	public Set<Map.Entry<String, IValue>> entrySet() {
		Map<String, IValue> news = new HashMap<>(this.arguments.size());
		this.arguments.forEach((key, value) -> news.put(key, new Value(http, serializer, value)));
		return news.entrySet();
	}

	@Override
	public Map<String, Object> getThis() {
		return this.arguments;
	}

	/**
	 * use cache rest in the future.
	 * @param value
	 * @return
	 */
	@Override
	public IRequestBody reset(Object value) {
		this.arguments = Cache.CacheHolder.getCache().map(client.style(), value);
		return this;
	}
}
