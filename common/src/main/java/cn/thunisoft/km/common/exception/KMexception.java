package cn.thunisoft.km.common.exception;

import cn.thunisoft.km.common.ifake.IError;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 16:04
 */
public class KMexception extends RuntimeException {

	private static final long serialVersionUID = 6437346099503373411L;

	protected IError error = IError.SYSTEM;
	protected Object[] values = null;
	protected int code = IError.SYSTEM.code();
	protected String text = "";

	public KMexception(){}

	public KMexception(String msg){
		super(msg);
	}

	public KMexception(Throwable t){
		super(t);
	}

	public KMexception(String msg, Throwable e){
		super(msg, e);
	}

	public KMexception(String msg, Object... args){
		super(IError.format(msg, args).intern());
		this.values = args;
		this.text = IError.format(msg, args).intern();
	}

	public KMexception(IError error, Throwable t, Object... args){
		super(error.format(args));
		this.error = error;
		this.values = args;
		this.code = error.code();
		this.text = IError.format(error.text(), args);
	}

	public Object[] getValues() {
		return values;
	}

	public String getText() {
		return text;
	}

	public int getCode() {
		return code;
	}

	public IError getError() {
		return error;
	}
}
