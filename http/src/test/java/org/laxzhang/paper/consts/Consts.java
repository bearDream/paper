package org.laxzhang.paper.consts;

import org.laxzhang.paper.auth.ITokenFactory;
import org.laxzhang.paper.auth.OAuth2Config;
import org.laxzhang.paper.oauth2.DefaultTokenFactory;
import org.laxzhang.paper.spring.SpringContext;

/**
 * This a demo, Please adjust params according to actual situation.
 * @author laxzhang@outlook.com
 * @Date 2019/4/24 11:42
 */
public final class Consts {

	public final static String PRO_SERVER;
	public final static String DEV_SERVER;
	public final static String ROOT;

	public final static String APP_ID;
	public final static String SECRET;
	public final static String CALLBACK_URL;
	public final static String AUTHORIZE_URL;
	public final static String ACCESSTOKEN_URL;
	public final static String LOGIN_URL;

	static {
		OAuth2Config config = SpringContext.getBeanIf(ITokenFactory.class, new DefaultTokenFactory()).loadConfig();

		ROOT = config.getServerUrl();
		APP_ID = config.getAppId();
		SECRET = config.getSecret();
		CALLBACK_URL = config.getCallBackUrl();
		AUTHORIZE_URL = ROOT + "/authorize";
		ACCESSTOKEN_URL = ROOT + "/token";
		LOGIN_URL = config.getLoginUrl();
		PRO_SERVER = ROOT;
		DEV_SERVER = ROOT;
	}
}
