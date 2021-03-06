package org.laxzhang.paper.handler;

import org.laxzhang.common.KM;
import org.laxzhang.paper.consts.Consts;
import org.laxzhang.paper.support.DefaultConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @TODO
 * @author laxzhang@outlook.com
 * @Date 2019/4/23 17:56
 */
public class DefaultRootX implements RootX {

	private final static Logger logger = LoggerFactory.getLogger(DefaultRootX.class);

	@Override
	public String root(String root) {

		if (KM.compareString(root, DefaultConsts.DEVELOPMENT)){
			return Consts.DEV_SERVER;
		}else if (KM.compareString(root, DefaultConsts.PRODUCTION)){
			return Consts.PRO_SERVER;
		}else {
			logger.error("root element only set DEVELOPMENT or PRODUCTION, Please check your @PaperClient root.");
			throw new IllegalArgumentException(" @PaperClient root is not correctly");
		}
	}
}
