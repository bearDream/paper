package cn.thunisoft.km.paper.bridge;

import java.util.Map;
import java.util.Set;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 15:51
 */
public interface IRequestBody {

	String serialize();

	IRequestBody put(String key, Object value);

	IValue get(String key);

	Set<Map.Entry<String, IValue>> entrySet();

	Map<String, Object> getThis();

	IRequestBody reset(Object value);
}
