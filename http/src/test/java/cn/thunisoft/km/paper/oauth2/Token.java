package cn.thunisoft.km.paper.oauth2;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/24 15:57
 */
@Data
@NoArgsConstructor
public class Token implements Serializable {

	private static final long serialVersionUID = 8317208097805560842L;

	@JSONField(name = "access_token")
	private String accessToken;

	@JSONField(name = "token_type")
	private String tokenType;

	@JSONField(name = "expires")
	private long expires;

	@JSONField(name = "refresh_token")
	private String refreshToken;
}
