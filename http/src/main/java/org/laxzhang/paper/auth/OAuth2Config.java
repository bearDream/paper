package org.laxzhang.paper.auth;

import java.io.Serializable;

import lombok.Data;

/**
 * General OAuth2 config parameters.
 * @author laxzhang@outlook.com
 * @Date 2019/4/24 15:50
 */
@Data
public class OAuth2Config implements Serializable {

	private static final long serialVersionUID = -6132782819437752867L;

	private String serverUrl;
	private String appId;
	private String secret;
	private String callBackUrl;
	private String loginUrl;

}
