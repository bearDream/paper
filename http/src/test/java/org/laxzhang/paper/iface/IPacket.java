package org.laxzhang.paper.iface;

import org.laxzhang.paper.Http;
import org.laxzhang.paper.PaperClient;
import org.laxzhang.paper.PathVariable;
import org.laxzhang.paper.beans.Packet;
import org.laxzhang.paper.handler.DefaultRootX;
import org.laxzhang.paper.handler.MyResultHandler;
import org.laxzhang.paper.support.DefaultConsts;

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
