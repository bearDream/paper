package cn.thunisoft.km.paper.handler;

import cn.thunisoft.km.common.KM;
import cn.thunisoft.km.paper.Http;
import cn.thunisoft.km.paper.support.XML;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.Optional;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/23 10:59
 */
public class DefaultParamSerialize implements ParamSerializer {

	/**
	 * serialize result.
	 * @param http
	 * @param o
	 * @return
	 */
	@Override
	public String serialize(Http http, Object o) {
		if (o == null){
			throw new IllegalArgumentException("argument is null. can not serialize. check @http body() and your parameters.");
		}
		if (KM.isBasicType(o.getClass())){
			return String.valueOf(o);
		}else if (http.request().oneOf(Http.Content.JSON)){
			return JSON.toJSONString(o);
		}else if (http.request().oneOf(Http.Content.XML)){
			return KM.uncheck(() -> XML.toString(o));
		}else if (http.request().oneOf(Http.Content.JAVA)){
			if (!KM.isEmpty((Map) o)){
				return JSONObject.toJSONString(((Map) o).get("arg0"));
			}
		}
		return JSON.toJSONString(o);
	}
}
