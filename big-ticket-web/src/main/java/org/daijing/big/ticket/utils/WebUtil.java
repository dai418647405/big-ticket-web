package org.daijing.big.ticket.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class WebUtil {

	private static Logger logger = LoggerFactory.getLogger(WebUtil.class);
	/**
	 * 生成签名信息
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getSignInfo(Map map, String encryptKey){
		Map sortedMap=new TreeMap(map); 
		String url="";
		for(Object obj:sortedMap.entrySet()){
			Entry entry=(Entry) obj;
			Object k=entry.getKey();
			Object v=entry.getValue();
			if ((null != v) && !"".equals(v) && !"signInfo".equals(k) && !"key".equals(k)) {
				url+=k+"="+v+"&";
			}
		}
		url+=encryptKey;
		String signinfo="";
		try {
			signinfo = DigestUtils.md5Hex(url.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("",e);
		}
		logger.info("对新结算url签名["+url+"]:"+signinfo);
		return signinfo;
	}
	
}
