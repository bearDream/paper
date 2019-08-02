package cn.thunisoft.km.paper.exceptions;

import cn.thunisoft.km.common.exception.KMexception;
import cn.thunisoft.km.common.ifake.IError;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/18 16:33
 */
public class PaperClientException extends KMexception {

	private static final long serialVersionUID = -6825802106282688420L;

	public PaperClientException(){
		super();
	}

	public PaperClientException(IError code, Throwable t, Object... args){
		super(code, t, args);
	}

	public PaperClientException(String msg){
		super(msg);
	}

	public PaperClientException(Throwable e){
		super(e);
	}

	public PaperClientException(String msg, Throwable e){
		super(msg, e);
	}

	public PaperClientException(String msg, Object... args){
		super(msg, args);
	}

}
