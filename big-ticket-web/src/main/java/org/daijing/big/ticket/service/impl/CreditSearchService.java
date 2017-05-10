//package org.daijing.big.ticket.service.impl;
//
///**企业信息查询
// * Created by daijing03 on 16/12/23.
// */
//
//import common.Logger;
//import net.sf.json.JSONObject;
//import org.daijing.big.ticket.utils.HttpUtil;
//import org.daijing.big.ticket.utils.WebUtil;
//
//import java.util.Map;
//
//public class CreditSearchService {
//    private final int CONNECT_TIMEOUT = 60 * 1000;
//
//    private final int READ_TIMEOUT = 60 * 1000;
//
//    private String id = "geetest";
//
//    private String key = "geetest";
//
//    private String url = "http://dev.cleverli.cn:8310";
//
//    private Logger logger = Logger.getLogger(this.getClass());
//
//    public JSONObject CreditSearchClient(Map<String, String> paramMap)throws Exception {
//        paramMap.put("appid",id);
//        paramMap.put("p","1");
//        String signInfo= WebUtil.getSignInfo(paramMap, key);
//        String kw = paramMap.get("kw");
//        String mt = paramMap.get("mt");
//        String dupUrl = url + "/api/qixinbao/company/search?kw="+kw+"&mt="+mt+"&p=1&appid="+id+"&signinfo="+signInfo;
//        logger.info("访问链接："+dupUrl);
//        //调用接口
//        String responseStr = HttpUtil.get(dupUrl, CONNECT_TIMEOUT,READ_TIMEOUT);
//        logger.info("最后获取数据："+responseStr);
//        //转换成list对象
//        JSONObject str = JSONObject.fromObject(responseStr);
//        return str;
//    }
//
//    public JSONObject Face(Map<String, String> paramMap)throws Exception {
////        File file = new File();
////        file.
////        paramMap.put("appid",id);
//        paramMap.put("p","1");
//        String signInfo= WebUtil.getSignInfo(paramMap, key);
//        String kw = paramMap.get("kw");
//        String mt = paramMap.get("mt");
//        String dupUrl = url + "/api/qixinbao/company/search?kw="+kw+"&mt="+mt+"&p=1&appid="+id+"&signinfo="+signInfo;
//        logger.info("访问链接："+dupUrl);
//        //调用接口
//        String responseStr = HttpUtil.get(dupUrl, CONNECT_TIMEOUT,READ_TIMEOUT);
//        logger.info("最后获取数据："+responseStr);
//        //转换成list对象
//        JSONObject str = JSONObject.fromObject(responseStr);
//        return str;
//    }
//
//}
