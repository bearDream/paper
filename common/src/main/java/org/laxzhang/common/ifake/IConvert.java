package org.laxzhang.common.ifake;

import org.laxzhang.common.annotations.Format;

/**
 * @see Format
 * @author laxzhang@outlook.com
 * @Date 2019/4/25 9:55
 */
public interface IConvert<From, To> {

	/**
	 * convert to value.
	 * @param value
	 * @return
	 */
	From from(To value);

	/**
	 * Convert from value.
	 * @param value
	 * @return
	 */
	To to(From value);

	class DefaultConvert implements IConvert<Object, Object>{

		@Override
		public Object from(Object value) {
			return value;
		}

		@Override
		public Object to(Object value) {
			return value;
		}
	}
}
