package cn.thunisoft.km.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.thunisoft.km.common.annotations.Format;
import cn.thunisoft.km.common.exception.KMexception;
import cn.thunisoft.km.common.functional.IExecutor;

/**
 * Common utils.
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 13:51
 */
public class KM {

	static Class<?>[] BASIC = new Class[]{String.class, Boolean.class, Character.class, Byte.class, Short.class, Integer.class,
				Long.class, Float.class, Double.class, Void.class};

	private static final Random RANDOM = new Random();

	public static boolean greedScans(Class<?> type){
		return null != type
				&& type != Object.class
				&& !isBasicType(type)
				&& !isProxyClass(type)
				&& !type.isInterface()
				&& !type.isEnum()
				&& !type.isAnnotation()
				&& null != type.getPackage()
				&& !isBlank(type.getPackage().getName())
				&& !type.getPackage().getName().startsWith("java");
	}

	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0){
			return true;
		}

		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))){
				return false;
			}
		}
		return true;
	}

	/**
	 * compare string, if any string is null, the result is false.
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean compareString(String str1, String str2){
		if (str1 == null || str2 == null){
			return false;
		}
		if (str1.compareTo(str2) == 0){
			return true;
		}
		return false;
	}

	private static boolean isProxyClass(Class<?> type){
		return isJdkProxy(type) && isCGLIBProxy(type);
	}

	private static boolean isCGLIBProxy(Class<?> clazz) {
		return clazz != null && (null != clazz.getName() && clazz.getName().contains("$$"));
	}

	private static boolean isJdkProxy(Class<?> clazz) {
		return clazz != null && Proxy.isProxyClass(clazz);
	}

	public static boolean isBasicType(Class<?> clazz) {
		if (clazz .isPrimitive()){
			return true;
		}
		for (Class<?> cls : BASIC) {
			if (cls.isAssignableFrom(clazz)){
				return true;
			}
		}
		return false;
	}


	public static <T> boolean isEmpty(T[] params) {
		return null == params || params.length < 1;
	}

	public static boolean isEmpty(Collection<?> collection){
		return null == collection || collection.isEmpty();
	}

	public static boolean isEmpty(Map<?, ?> parameters){
		return null == parameters || parameters.isEmpty();
	}

	public static <T> T newInstance(Class<T> clazz) {
		return uncheck(clazz::newInstance);
	}

	/**
	 * uncheck invoke method. See java checked exception and unchecked exception.
	 * Just eat a exception.
	 * @param executor
	 * @param <T>
	 * @return
	 */
	public static <T> T uncheck(IExecutor<T> executor){
		try {
			return executor.execute();
		}catch (Throwable e){
			throw new KMexception(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <TYPE> TYPE cast(String value, Class<TYPE> type) {
		if (type == String.class) {
			return (TYPE) value;
		}
		boolean blank = isBlank(value);
		if (type == Character.class || type == char.class) {
			return (TYPE) (blank ? Character.valueOf(' ') : Character.valueOf(value.charAt(0)));
		}
		if (type == Boolean.class || type == boolean.class) {
			return (TYPE) (blank ? Boolean.valueOf(false) : Boolean.valueOf(value));
		}
		if (type == Byte.class || type == byte.class) {
			return (TYPE) (null == value ? null : value.getBytes());
		}
		if (type == Short.class || type == short.class) {
			return (TYPE) Short.valueOf(cast(value, BigDecimal.class).shortValue());
		}
		if (type == Integer.class || type == int.class) {
			return (TYPE) Integer.valueOf(cast(value, BigDecimal.class).intValue());
		}
		if (type == Long.class || type == long.class) {
			return (TYPE) Long.valueOf(cast(value, BigDecimal.class).longValue());
		}
		if (type == Float.class || type == float.class) {
			return (TYPE) Float.valueOf(cast(value, BigDecimal.class).floatValue());
		}
		if (type == Double.class || type == double.class) {
			return (TYPE) Double.valueOf(cast(value, BigDecimal.class).doubleValue());
		}
		if (type == BigDecimal.class) {
			return (TYPE) (blank ? BigDecimal.ZERO : removePercent(value));
		}
		if (type == Date.class) {
			return (TYPE) (blank ? null : new SimpleDateFormat("yyyyMMddHHmmss").parse(standardDateTime(value), new ParsePosition(0)));
		}
		if (type == LocalDateTime.class) {
			return (TYPE) (blank ? null : parse(value));
		}
		return (TYPE) (blank ? null : value);
	}

	private static BigDecimal removePercent(String text) {
		if (text.contains("%")) {
			return new BigDecimal((text.replace("%", ""))).movePointLeft(2);
		}
		if (text.contains("‰")) {
			return new BigDecimal((text.replace("‰", ""))).movePointLeft(3);
		}
		return new BigDecimal(text);
	}

	public static LocalDateTime parse(String text) {
		return LocalDateTime.parse(standardDateTime(text), DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.CHINA));
	}

	private static String standardDateTime(String text) {
		String standard = text.replaceAll("\\D", "");
		StringBuilder bu = new StringBuilder(standard);
		while (bu.length() < 14) {
			if ((bu.length() == 5 && bu.charAt(4) == '0') || (bu.length() == 7 && bu.charAt(6) == '0')) {
				bu.append('1');
			} else {
				bu.append('0');
			}
		}
		standard = bu.toString();
		if (standard.length() > 14) {
			standard = standard.substring(0, 14);
		}
		return standard;
	}

	public static <T> T pick(Supplier<T>... suppliers) {
		if (isEmpty(suppliers)){
			return null;
		}
		for (Supplier<T> s : suppliers) {
			T v = s.get();
			if (null != v){
				return v;
			}
		}
		return null;
	}

	public static <T> T catchThen(IExecutor<T> done, Supplier<T> readyDo){
		try {
			return done.execute();
		}catch (Throwable e){
			return readyDo.get();
		}
	}

	public static String randomNumeric(int size) {
		char[] chars = new char[size];
		for (int i = 0; i < size; i++) {
			chars[i] = (char) (48 + RANDOM.nextInt(10));
		}
		return new String(chars);
	}

	/**
	 * Determine e is extends te or te
	 * @param e
	 * @param te
	 * @return
	 */
	public static boolean isBelongToThrowable(Throwable e, Class<? extends Throwable> te) {
		if (null == e || te == null){
			return false;
		}
		if (te.isAssignableFrom(e.getClass()) || te.isInstance(e)){
			return true;
		}
		return false;
	}

	/**
	 * find Annotation from field, if its no clazz annotation, then find from method.
	 * @param field
	 * @param method
	 * @param clazz annotation.
	 * @param <T>
	 * @return
	 */
	public static <T extends Annotation> T findAnnotation(Field field, Method method, Class<T> clazz) {
		return field.isAnnotationPresent(clazz) ? field.getAnnotation(clazz) : method.getAnnotation(clazz);
	}

	/**
	 * convert hump to underline.
	 * @param str
	 * @return
	 */
	public static String humpToLine(String str) {
		return str.replaceAll("[A-Z]", "_$0").toLowerCase();
	}

	private static Pattern linePattern = Pattern.compile("_(\\w)");

	/**
	 * convert underline to hump.
	 * @param str
	 * @return
	 */
	public static String lineToHump(String str) {
		str = str.toLowerCase();
		Matcher matcher = linePattern.matcher(str);
		StringBuffer bu = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(bu, matcher.group(1).toUpperCase());
		}
		matcher.appendTail(bu);
		return bu.toString();
	}

	/**
	 * Convert as {@link Date}, {@link LocalDateTime}, {@link LocalDate}, {@link LocalTime}, {@link Number} to specify former.
	 * Only format Date time and Number.
	 * @param value
	 * @param format
	 * @return
	 */
	public static Object format(Object value, String format) {

		if (value == null){
			return "";
		}
		if (isBlank(format)){
			return String.valueOf(value);
		}
		Class<?> clazz = value.getClass();

		if (clazz == Date.class){
			return new SimpleDateFormat(format).format(value);
		}

		if (clazz == LocalDateTime.class){
			if ("millsecond".equals(format)){
				return String.valueOf(((LocalDateTime) value).toInstant(OffsetDateTime.now().getOffset()).toEpochMilli());
			}
			if ("second".equals(format)){
				return String.valueOf(((LocalDateTime) value).toInstant(OffsetDateTime.now().getOffset()).getEpochSecond());
			}
			return ((LocalDateTime) value).format(DateTimeFormatter.ofPattern(format));
		}

		if (clazz == LocalDate.class){
			return ((LocalDate) value).format(DateTimeFormatter.ofPattern(format));
		}

		if (clazz == LocalTime.class){
			return ((LocalTime) value).format(DateTimeFormatter.ofPattern(format));
		}

		// Convert number as a specify mode.
		if (Number.class.isAssignableFrom(clazz)){
			if (int.class == clazz || Integer.class == clazz || long.class == clazz || Long.class == clazz){
				return String.valueOf(value);
			}
			boolean dot = format.contains(".0");
			boolean hun = format.contains("%");
			// if contains "%", the scale must deduce 2, otherwise deduce 1.
			int scale = dot ? (format.length() - format.indexOf(".0") - (hun ? 2 : 1)) : 2;

			// if not bigDecimal, convert it and recursive deal with.
			if (BigDecimal.class != clazz){
				return format(new BigDecimal(((Number) value).doubleValue()), format);
			}
			if (hun){
				return ((BigDecimal) value).movePointRight(2).setScale(scale, RoundingMode.HALF_UP).toString() + "%";
			}

			return ((BigDecimal) value).setScale(scale, RoundingMode.HALF_UP).toString();
		}

		return String.valueOf(value);
	}

	public static String toString(byte[] bytes, String charset){
		return new String(bytes, charset == null ? Charset.defaultCharset() : Charset.forName(charset));
	}

	public static byte[] toByteArray(InputStream input) {
		return uncheck(() -> {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int n = 0;
			while (-1 != (n = input.read(buffer))){
				output.write(buffer, 0, n);
			}
			return output.toByteArray();
		});
	}
}
