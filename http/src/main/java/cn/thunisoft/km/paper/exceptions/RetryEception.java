package cn.thunisoft.km.paper.exceptions;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/26 18:26
 */
public class RetryEception extends PaperClientException {

	private static final long serialVersionUID = 5519660806747442605L;

	public RetryEception(){}

	public RetryEception(String msg, Throwable t, Object... args){
		super(msg, args);
	}

	public RetryEception(String msg, Object... args){
		super(msg, args);
	}

	public RetryEception(Throwable t){
		super(t);
	}
}
