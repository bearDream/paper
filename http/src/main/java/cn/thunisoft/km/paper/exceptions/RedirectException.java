package cn.thunisoft.km.paper.exceptions;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/23 10:31
 */
public class RedirectException extends PaperClientException {

	private static final long serialVersionUID = -5618709372199697870L;

	private String redirect;

	public RedirectException(String msg, Object... args){
		super(msg, args);
		this.redirect = msg;
	}

	public String getRedirect() {
		return redirect;
	}
}
