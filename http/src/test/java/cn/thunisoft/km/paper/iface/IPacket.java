package cn.thunisoft.km.paper.iface;

import cn.thunisoft.km.paper.Http;
import cn.thunisoft.km.paper.PaperClient;
import cn.thunisoft.km.paper.PathVariable;
import cn.thunisoft.km.paper.beans.Packet;
import cn.thunisoft.km.paper.handler.DefaultRootX;
import cn.thunisoft.km.paper.handler.MyResultHandler;
import cn.thunisoft.km.paper.support.DefaultConsts;

/**
 * @author laxzhang@outlook.com
 * @Date 2019/4/28 11:31
 */
@PaperClient(root = DefaultConsts.DEVELOPMENT, rootX = {DefaultRootX.class}, result = MyResultHandler.class)
public interface IPacket {

	/**
	 * 查询快递公司单号
	 * @param type
	 * @param postId
	 * @return
	 */
	@Http(path = "/query", response = Http.Content.JSON)
	Packet hello(@PathVariable("type") String type, @PathVariable("postid") String postId);
}
