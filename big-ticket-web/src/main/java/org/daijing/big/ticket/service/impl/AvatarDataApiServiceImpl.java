package org.daijing.big.ticket.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.daijing.big.ticket.service.AvatarDataApiService;
import org.daijing.big.ticket.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by daijing03 on 17/5/2.
 */
@Service("avatarDataApiServiceImpl")
public class AvatarDataApiServiceImpl implements AvatarDataApiService {

    private static final Logger logger = LoggerFactory.getLogger(AvatarDataApiService.class);

    public JSONObject getLatestJokeImg(Integer page, Integer rows) {
        if (page == null || rows == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        String url = sb.append(JOKE_HOST)
                .append("/Joke/NewstImg?key=")
                .append(JOKE_APP_TOKEN)
                .append("&page=")
                .append(page)
                .append("&rows=")
                .append(rows).toString();
        String responseStr = HttpUtil.get(url, CONNECT_TIMEOUT,READ_TIMEOUT);
        if (responseStr != null && !responseStr.trim().equals("")) {
            return JSON.parseObject(responseStr);
        }
//        } else {
//            return JSON.parseObject("    {\n" +
//                    "\n" +
//                    "\n" +
//                    "      \"result\": [\n" +
//                    "\n" +
//                    "\n" +
//                    "        {\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"content\": \"穿上衣服变傻的孩子\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"hashId\": \"A15462E63B747CA3CF5EEC14C0CEFFF2\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"unixtime\": \"1448008392\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"updatetime\": \"2015-11-20 16:33:12\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"url\": \"http://api.avatardata.cn/Joke/Img?file=41398022271e4eadb8d040aa97ecd715.jpg\"\n" +
//                    "\n" +
//                    "\n" +
//                    "        },\n" +
//                    "\n" +
//                    "\n" +
//                    "        {\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"content\": \"最美的辫子\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"hashId\": \"EC385E687D8BB4DC90A08CE015C39EDE\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"unixtime\": \"1448008392\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"updatetime\": \"2015-11-20 16:33:12\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"url\": \"http://api.avatardata.cn/Joke/Img?file=226005a521764c478ba21b0d6814b3d5.jpg\"\n" +
//                    "\n" +
//                    "\n" +
//                    "        },\n" +
//                    "\n" +
//                    "\n" +
//                    "        {\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"content\": \"我只想做个安静的吃货\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"hashId\": \"0144DE4E8500D43E4257FC44CBB657C9\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"unixtime\": \"1448008392\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"updatetime\": \"2015-11-20 16:33:12\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"url\": \"http://api.avatardata.cn/Joke/Img?file=995f9e486f694b719e42758ec0c7ddca.gif\"\n" +
//                    "\n" +
//                    "\n" +
//                    "        },\n" +
//                    "\n" +
//                    "\n" +
//                    "        {\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"content\": \"值得尊敬的人\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"hashId\": \"4BC7A8126AFA06C2F5472E422F27413A\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"unixtime\": \"1448008392\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"updatetime\": \"2015-11-20 16:33:12\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"url\": \"http://api.avatardata.cn/Joke/Img?file=1fef61eb371147b7b0fd35927b61cedb.jpg\"\n" +
//                    "\n" +
//                    "\n" +
//                    "        },\n" +
//                    "\n" +
//                    "\n" +
//                    "        {\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"content\": \"洗衣服也要有范\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"hashId\": \"135A5E9C6FC38DB7D0CE346B8F58D126\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"unixtime\": \"1448008392\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"updatetime\": \"2015-11-20 16:33:12\",\n" +
//                    "\n" +
//                    "\n" +
//                    "          \"url\": \"http://api.avatardata.cn/Joke/Img?file=ba8a894596134642b9dbcacf61188107.gif\"\n" +
//                    "\n" +
//                    "\n" +
//                    "        }\n" +
//                    "\n" +
//                    "\n" +
//                    "      ],\n" +
//                    "\n" +
//                    "\n" +
//                    "      \"error_code\": 0,\n" +
//                    "\n" +
//                    "\n" +
//                    "      \"reason\": \"Succes\"\n" +
//                    "\n" +
//                    "\n" +
//                    "    }");
//        }
        return null;
    }

}
