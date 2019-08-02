package cn.thunisoft.km.paper.bridge;

import java.net.URLEncoder;

import cn.thunisoft.km.common.KM;

/**
 * Query builder.
 * @author laxzhang@outlook.com
 * @Date 2019/4/22 15:49
 */
public class RequestQuery implements IRequestQuery {

	private boolean encode = false;
	private StringBuilder location;

	public RequestQuery(String url, boolean urlencode) {
		this.location = new StringBuilder(url);
		this.encode = urlencode;
		if (!url.contains("?")){
			this.location.append("?");
		}
	}


	@Override
	public IRequestQuery clone() {
		IRequestQuery requestQuery = new RequestQuery(toString(), this.encode);
		return requestQuery;
	}

	/**
	 * Add query param to Url.
	 * Deal with different url(with ? or without ?)(with & or without &)
	 * For example: url like http://www.google.com param like name->admin
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public IRequestQuery addQuery(String key, Object value) {
		char last = this.location.charAt(this.location.length() - 1);
		if (last != '&' && last != '?'){
			this.location.append("&");
		}
		this.location.append(key).append('=').append(String.valueOf(value)).append('&');
		return this;
	}

	@Override
	public String toString() {
		char last = this.location.charAt(this.location.length() - 1);
		boolean cut = last == '&' || last == '?';
		final String url = cut ? this.location.substring(0, this.location.length() - 1) : this.location.toString();
		return encode ? KM.uncheck(() -> URLEncoder.encode(url, "UTF-8")) : url;
	}
}
