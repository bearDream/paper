package cn.thunisoft.km.paper.enums;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/19 16:31
 */
public enum ResponseStatus {

	/**
	 * Response status code.
	 */
	SUCCESS(200, "Request success"),
	FAILED(-2, "Request failed"),
	ERROR(-4, "Internal Error"),
	PARSE_ERROR(-8, "PARSE DATA ERROR"),
	NO_AUTHORIZED(-16, "NO AUTHORIZED.");

	private int code;
	private String msg;

	ResponseStatus(int code, String msg){
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
