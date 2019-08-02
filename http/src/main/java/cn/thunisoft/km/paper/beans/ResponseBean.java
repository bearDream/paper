package cn.thunisoft.km.paper.beans;

import cn.thunisoft.km.paper.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Default response entity.
 * @author laxzhang@outlook.com
 * @Date 2019/4/19 16:26
 */
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ResponseBean {

	private int code;
	private String msg;
	private Object data;
}
