package org.daijing.big.ticket.utils;

/**
 * Created by daijing03 on 17/6/28.
 */
public final class SpiderConstant {

    private SpiderConstant() {}

    //话题总共展示的数量
    public static final int TOPIC_SHOW_NUM = 1500;

    //批量更新发表时间的个数
    public static final int BATCH_NUM = 100;

    //爬虫下载失败重试次数
    public static final int DOWNLOAD_FAILED_CYCLE_RETRY_TIMES = 1;

    //httpclient连接超时重试次数
    public static final int HTTP_CLIENT_CONN_TIMEOUT_RETRY_TIMES = 1;

    //抓取间隔时间(ms)
    public static final int CRAWL_TIME_INTERVAL = 2500;

    //http请求socket链接超时时间
    public static final int HTTP_SOCKET_TIME_OUT = 5 * 1000;
}
