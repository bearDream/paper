package org.laxzhang.paper.oauth2;

import org.laxzhang.paper.auth.ITokenFactory;
import org.laxzhang.paper.auth.OAuth2Config;
import lombok.extern.slf4j.Slf4j;

/**
 * Token factory.
 * @TODO
 * @author laxzhang@outlook.com
 * @Date 2019/4/24 15:55
 */
@Slf4j(topic = "Default Token Factory")
public class DefaultTokenFactory implements ITokenFactory {

	private Token token;

	@Override
	public String getToken(Long id, int fails) {
		if (token == null){
			init();
		}
		log.info("Token Factory get token...");
		return token.getAccessToken();
	}

	@Override
	public OAuth2Config loadConfig() {
		log.info("Token Factory load config...");
		OAuth2Config config = new OAuth2Config();
		config.setServerUrl("http://www.kuaidi100.com");
		config.setAppId("wx8e08cc0cbb40168c");
		config.setSecret("wx8e08cc0cbb40168c");
		return config;
	}

	private void init(){

	}
}
