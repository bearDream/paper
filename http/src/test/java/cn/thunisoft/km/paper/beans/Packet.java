package cn.thunisoft.km.paper.beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/26 16:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Packet implements Serializable {

	private static final long serialVersionUID = -3516854346857835127L;
	private String message;
	private String nu;
	private String ischeck;
	private String condition;
	private String com;
	private String status;
	private String state;
	private List<Details> data;

	@Data
	@NoArgsConstructor
	@ToString
	class Details implements Serializable{
		private static final long serialVersionUID = -766011866860816981L;
		private LocalDateTime time;
		private LocalDateTime ftime;
		private String context;
		private String location;
	}
}
