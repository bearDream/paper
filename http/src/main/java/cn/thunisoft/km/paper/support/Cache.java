package cn.thunisoft.km.paper.support;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.annotation.XmlElement;

import cn.thunisoft.km.common.KM;
import cn.thunisoft.km.common.annotations.Format;
import cn.thunisoft.km.common.ifake.IConvert;
import cn.thunisoft.km.paper.PaperClient;
import cn.thunisoft.km.paper.PathVariable;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * memory cache util and resolve params.
 * @author laxzhang@outlook.com
 * @Date 2019/4/24 18:08
 */
public final class Cache {

	private Cache(){

	}

	private Map<Class<?>, Map<Field, Method>> cache = new ConcurrentHashMap<>();
	private Map<Class<?>, IConvert> converts = new ConcurrentHashMap<>();

	/**
	 * Get converted bean map, if not cached, it will be cache.
	 * @param style
	 * @param bean
	 * @return
	 */
	public Map<String, Object> map(int style, Object bean){

		Map<Field, Method> cacheMap = get(bean.getClass());
		Map<String, Object> map = new HashMap<>(cacheMap.size());

		if (KM.isEmpty(cacheMap)){
			return map;
		}
		cacheMap.forEach((key, value) -> {
			Object v = resolveValue(bean, key, value);
			if (v == null){
				return;
			}
			String k = resolveKey(style, key, value);
			map.put(k, v);
		});
		return map;
	}

	/**
	 * resolve params key as for specify patters.
	 * If field has {@link Format}, {@link JSONField}, then convert its name to hump or underline according to whether has value.
	 * @see Format
	 * @param style hump or underline
	 * @param field
	 * @param method
	 * @return
	 */
	public String resolveKey(int style, Field field, Method method) {
		Format format = KM.findAnnotation(field, method, Format.class);
		if (format != null && !KM.isBlank(format.value())){
			return format.value();
		}
		JSONField jsonField = KM.findAnnotation(field, method, JSONField.class);
		if (jsonField != null && !KM.isBlank(jsonField.name())){
			return jsonField.name();
		}
		XmlElement xmlElement = KM.findAnnotation(field, method, XmlElement.class);
		if (xmlElement != null && !KM.isBlank(xmlElement.name())){
			return xmlElement.name();
		}
		PathVariable pathVariable = KM.findAnnotation(field, method, PathVariable.class);
		if (pathVariable != null && !KM.isBlank(pathVariable.value())){
			return pathVariable.value();
		}
		String key = field.getName();
		return humpOrLineConverter(style, key);
	}

	/**
	 * Get converted key.
	 * @param style hump or underline
	 * @param parameter
	 * @return
	 */
	public String resolveKey(int style, Parameter parameter){
		Format format = parameter.getAnnotation(Format.class);
		if (null != format && !KM.isBlank(format.value())){
			return format.value();
		}
		JSONField jsonField = parameter.getAnnotation(JSONField.class);
		if (null != jsonField && !KM.isBlank(jsonField.name())){
			return jsonField.name();
		}
		XmlElement xmlElement = parameter.getAnnotation(XmlElement.class);
		if (null != xmlElement && !KM.isBlank(xmlElement.name())){
			return xmlElement.name();
		}
		PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
		if (pathVariable != null && !KM.isBlank(pathVariable.value())){
			return pathVariable.value();
		}
		String key = parameter.getName();
		return humpOrLineConverter(style, key);
	}

	private String humpOrLineConverter(int style, String key){
		if (style == PaperClient.HUMP){
			return KM.lineToHump(key);
		}
		if (style == PaperClient.LINE){
			return KM.humpToLine(key);
		}
		return key;
	}


	/**
	 * Get invoked method's value and format it.
	 * @param bean
	 * @param field
	 * @param method
	 * @return
	 */
	public Object resolveValue(Object bean, Field field, Method method) {

		Format format = KM.findAnnotation(field, method, Format.class);
		Object value = KM.uncheck(() -> method.invoke(bean));
		if (value == null){
			return value;
		}
		// If use specify convert class.
		if (format != null && IConvert.DefaultConvert.class != format.convert()){
			converts.putIfAbsent(format.convert(), KM.uncheck(() -> format.convert().newInstance()));
			return converts.get(format.convert()).to(value);
		}

		if (format != null && !KM.isBlank(format.format())){
			return KM.format(value, format.format());
		}
		JSONField jsonField = KM.findAnnotation(field, method, JSONField.class);
		if (jsonField != null && KM.isBlank(jsonField.format())){
			return KM.format(value, jsonField.format());
		}
		return value;
	}

	/**
	 * Only format value.
	 * @param val
	 * @param parameter
	 * @return
	 */
	public Object resolveValue(Object val, Parameter parameter){
		if (val == null){
			return val;
		}

		Format format = parameter.getAnnotation(Format.class);
		if (null != format && IConvert.DefaultConvert.class != format.convert()){
			converts.putIfAbsent(format.convert(), KM.uncheck(() -> format.convert().newInstance()));
			return converts.get(format.convert()).to(val);
		}

		if (null != format && !KM.isBlank(format.format())){
			return KM.format(val, format.format());
		}

		JSONField jsonField = parameter.getAnnotation(JSONField.class);
		if (null != jsonField && !KM.isBlank(jsonField.format())){
			return KM.format(val, format.format());
		}
		return val;
	}



	public Map<Field, Method> get(Class<?> clazz){
		if (cache.get(clazz) == null){
			cache(clazz);
		}
		return cache.get(clazz);
	}

	/**
	 * Only cache Getter method.
	 * @param clazz
	 */
	private void cache(Class<?> clazz) {
		Map<Field, Method> fieldMethod = new HashMap<>();
		List<Field> fields = greedCollectAllField(clazz, new ArrayList<>());
		Method[] methods = clazz.getDeclaredMethods();
		Map<String, Field> mapFields = new HashMap<>();
		// convert list of fields to map of fields
		for (Field field : fields) {
			mapFields.put(field.getName().toLowerCase(), field);
		}

		for (Method method : methods) {
			String name = method.getName().toLowerCase();
			if (!name.startsWith("get")){
				continue;
			}
			name = name.substring(3);
			Field field = mapFields.get(name);
			if (field == null){
				continue;
			}
			fieldMethod.put(field, method);
		}
		cache.put(clazz, fieldMethod);
	}

	/**
	 * Get clazz and its super class's all of fields.
	 * @param clazz
	 * @param fields
	 * @return
	 */
	private List<Field> greedCollectAllField(Class<?> clazz, List<Field> fields){
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		if (KM.greedScans(clazz.getSuperclass())){
			return greedCollectAllField(clazz.getSuperclass(), fields);
		}
		return fields;
	}

	/**
	 * To use cache directly.
	 */
	public static class CacheHolder{

		private final static Cache CACHE = new Cache();

		public static Cache getCache(){
			return CACHE;
		}
	}
}
