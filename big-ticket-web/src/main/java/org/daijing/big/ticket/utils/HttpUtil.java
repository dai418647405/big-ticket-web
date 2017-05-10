package org.daijing.big.ticket.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 短信http接口的java代码调用示例 基于Apache HttpClient 4.3
 * 
 * @author noreplyliz
 * @date 2015-10-26
 */

public class HttpUtil {

    private static final Logger logger = Logger.getLogger(HttpUtil.class);
    private static String CHARSET = "UTF-8";
    private static final String APPLICATION_JSON = "application/json";

    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    public static String get(String url) {
        return get(url, 0, 0);
    }

    public static String get(String url, int connectTimeoutMillSec, int readTimeoutMillSec) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpGet.addHeader("Connection", "Keep-Alive");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
            httpGet.addHeader("Cookie", "");
            if (connectTimeoutMillSec > 10000 && readTimeoutMillSec > 10000) {
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(connectTimeoutMillSec).setConnectTimeout(readTimeoutMillSec).build();// 设置请求和传输超时时间
                httpGet.setConfig(requestConfig);
            }

            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;

    }

    public static void main(String[] args) {
        //210.22.89.58:37278
        final String url = "http://127.0.0.1:8000/sxd/nativeapp/service.html?method=sxdorder.create.sxdorder.info&user_id=47";
        final String json = "{afdsaf:fadsfdsafdsa:fadsfadsfdsf}";
        
//        System.out.println("final" + " : " + post(url, EncryptUtil.desEncrypt(json,EncryptUtil.SALT_KEY)));
    }

    public static String post(String url, String paramStr) {
        return post(url, paramStr, 0, 0);
    }

    public static String post(String url, String paramStr, int connectTimeoutMillSec, int readTimeoutMillSec) {
        CloseableHttpClient client = HttpClients.createDefault();

        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);

            method.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

            if (connectTimeoutMillSec > 10000 && readTimeoutMillSec > 10000) {
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(connectTimeoutMillSec).setConnectTimeout(readTimeoutMillSec).build();// 设置请求和传输超时时间
                method.setConfig(requestConfig);
            }
            StringEntity se = new StringEntity(paramStr, CHARSET);
            se.setContentType(CONTENT_TYPE_TEXT_JSON);
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
            method.setEntity(se);

            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                logger.error(e);
            }

            try {
                client.close();
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return responseText;
    }

    /**
     * 基于HttpClient 4.3的通用POST方法
     * 
     * @param url
     *            提交的URL
     * @param paramsMap
     *            提交<参数，值>Map
     * @return 提交响应
     */

    public static String post(String url, Map<String, String> paramsMap) {
        return post(url, paramsMap, 0, 0);
    }

    public static String post(String url, Map<String, String> paramsMap, int connectTimeoutMillSec, int readTimeoutMillSec) {
        CloseableHttpClient client = HttpClients.createDefault();

        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);

            if (connectTimeoutMillSec > 10000 && readTimeoutMillSec > 10000) {
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(connectTimeoutMillSec).setConnectTimeout(readTimeoutMillSec).build();// 设置请求和传输超时时间
                method.setConfig(requestConfig);
            }

            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, CHARSET));
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return responseText;
    }

}