package org.laxzhang.paper.handler;

import org.laxzhang.paper.PaperClient;

/**
 * For checkout root path.
 * @author laxzhang@outlook.com
 * @Date 2019/4/23 17:55
 */
public interface RootX {

	/**
	 * select root.
	 * @param root {@link PaperClient#root()}
	 * @return
	 */
	String root(String root);
}
