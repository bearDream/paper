package org.laxzhang.paper.auth;


/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/24 15:53
 */
public interface ITokenFactory {

	String getToken(Long id, int fails);

	/**
	 * Load OAuth2 config.
	 * @return
	 */
	OAuth2Config loadConfig();

	default String getAccessToken(Long id, int fails){
		return getToken(id, fails);
	}
}
