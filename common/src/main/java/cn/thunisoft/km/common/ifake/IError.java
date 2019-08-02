package cn.thunisoft.km.common.ifake;

import java.io.Serializable;
import java.util.regex.Pattern;

import cn.thunisoft.km.common.KM;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 16:10
 */
public interface IError extends IReturnCode, Serializable {

	int code();

	String text();

	String cause();

	IError SYSTEM = new IError() {
		private static final long serialVersionUID = -3931605389602818263L;

		@Override
		public int code() {
			return 999;
		}

		@Override
		public String text() {
			return "系统异常";
		}

		@Override
		public String cause() {
			return "系统异常";
		}

	};

	@Override
	default String getCode() {
		return code() + "";
	}

	@Override
	default String getMsg(){
		return format(text());
	}

	Pattern split = Pattern.compile("\\{\\s*\\}");

	default String format(Object... args) {
		String formatter = cause();
		return format(formatter, args);
	}

	static String format(String formatter, Object... args){
		if (null == args || args.length < 1 || null == formatter || !split.matcher(formatter).find()) {
			return KM.pick(() -> formatter, () -> "");
		}
		StringBuilder bu = new StringBuilder();
		String[] cuts = formatter.split(split.pattern());
		for (int index = 0; index < cuts.length; index++) {
			bu.append(cuts[index]);
			if (index < args.length) {
				bu.append(args[index]);
			} else if (index < cuts.length - 1) {
				// bu.append("{}");
			}
		}
		return bu.toString();
	}
}
