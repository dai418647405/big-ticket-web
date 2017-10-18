package org.daijing.big.ticket.crawler.hupu.spider;

import lombok.Getter;
import lombok.Setter;
import org.daijing.big.ticket.crawler.hupu.SpiderScheduler;
import org.daijing.big.ticket.crawler.hupu.pipeline.ArticleDbPipeline;
import org.daijing.big.ticket.crawler.hupu.processor.ArticleListPageProcessor;
import org.daijing.big.ticket.service.CacheRefreshService;
import org.daijing.big.ticket.utils.SpiderConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by daijing03 on 17/5/26.
 */
public class HupuSpider implements SpiderScheduler {

    private Logger logger = LoggerFactory.getLogger(HupuSpider.class);

    @Getter
    @Setter
    private String taskName;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private Integer threadNum = 20;

    @Getter
    @Setter
    private boolean needCookie;

    @Getter
    @Setter
    private PageProcessor processor;

    @Qualifier("articleDbPipeline")
    @Autowired
    private ArticleDbPipeline articleDbPipeline;

    @Qualifier("cacheRefreshServiceImpl")
    @Autowired
    private CacheRefreshService cacheRefreshService;

    @Getter
    @Setter
    private Integer limitPageNumber;

    private HttpClientDownloader httpClientDownloader = new HttpClientDownloader();

    private static final int maxProxyNum = 5;

    public HupuSpider() {}

    private void setProxyProvider() {
        BufferedReader proxyIpBr = null;
        try {
            proxyIpBr = new BufferedReader(new FileReader("/data/appdatas/big-ticket-web/proxy_ip.csv"));
            String line;
            String[] ipAndPort;
            List<Proxy> proxyList = new ArrayList<Proxy>(maxProxyNum);
            while ((line = proxyIpBr.readLine()) != null) {
                ipAndPort = line.split(":");
                proxyList.add(new Proxy(ipAndPort[0], Integer.valueOf(ipAndPort[1])));
            }
            if (!proxyList.isEmpty()) {
                httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(proxyList.toArray(new Proxy[proxyList.size()])));
            } else {
                logger.error("代理ip为空!将使用本地ip爬取!");
                httpClientDownloader.setProxyProvider(null);
            }
        } catch (Exception e) {
            logger.error("设置代理ip失败", e);
            httpClientDownloader.setProxyProvider(null);
        } finally {
            if (proxyIpBr != null) {
                try {
                    proxyIpBr.close();
                } catch (IOException e) {
                    logger.error("关闭代理文件reader异常", e);
                }
            }
        }
    }

    private boolean permitCrawl() {
        BufferedReader permitCrawlBr = null;
        try {
            permitCrawlBr = new BufferedReader(new FileReader("/data/appdatas/big-ticket-web/permit_crawl.csv"));
            String line = permitCrawlBr.readLine();
            if (line != null && !line.trim().equals("")) {
                return Boolean.parseBoolean(line);
            }
        } catch (Exception e) {
            logger.error("查询是否允许爬取失败,默认允许", e);
        } finally {
            if (permitCrawlBr != null) {
                try {
                    permitCrawlBr.close();
                } catch (IOException e) {
                    logger.error("关闭爬取标志文件reader异常", e);
                }
            }
        }
        return true;
    }

    private boolean permitCrawlTime() {
        BufferedReader crawlTimeBr = null;
        try {
            //读取配置值
            crawlTimeBr = new BufferedReader(new FileReader("/data/appdatas/big-ticket-web/crawl_time.properties"));
            Properties prop = new Properties();
            prop.load(crawlTimeBr);
            String startTimeProp = prop.getProperty("startTime", "00:00");
            String endTimeProp = prop.getProperty("endTime", "23:59");
            String startTime1Prop = prop.getProperty("startTime1", "00:00");
            String endTime1Prop = prop.getProperty("endTime1", "23:59");
            //格式化时间
            Date now = new Date();
            DateFormat df = new SimpleDateFormat("HH:mm");
            Date startTime = df.parse(startTimeProp);
            Date endTime = df.parse(endTimeProp);
            Date startTime1 = df.parse(startTime1Prop);
            Date endTime1 = df.parse(endTime1Prop);
            Date curTime = df.parse(df.format(now));
            return inDateRange(curTime, startTime, endTime) || inDateRange(curTime, startTime1, endTime1);
        } catch (Exception e) {
            logger.error("获取允许爬取时间段失败", e);
        } finally {
            if (crawlTimeBr != null) {
                try {
                    crawlTimeBr.close();
                } catch (IOException e) {
                    logger.error("关闭爬取时间段reader异常", e);
                }
            }
        }
        return false;
    }

    private boolean inDateRange(Date curTime, Date startTime, Date endTime) {
        return curTime.equals(startTime) || curTime.equals(endTime) || (curTime.after(startTime) && curTime.before(endTime));
    }

    private void setCookie(Site site) {
        try {
            site.getCookies().clear();
            //读取配置值
            BufferedReader cookieBr = new BufferedReader(new FileReader("/data/appdatas/big-ticket-web/cookie.properties"));
            Properties prop = new Properties();
            prop.load(cookieBr);
            for (String cookieProp : prop.stringPropertyNames()) {
                site.addCookie(cookieProp, prop.getProperty(cookieProp));
            }
        } catch (Exception e) {
            logger.error("设置cookie失败", e);
        }
    }

    @Async
    @Override
    public void schedule() {
        if (!permitCrawl()) {
            logger.error("禁止爬取!");
            return;
        }
        if (!permitCrawlTime()) {
            logger.error("不在爬取时间段内,禁止爬取");
            return;
        }
        this.setProxyProvider();
        DateFormat hourDf = new SimpleDateFormat("HH");
        DateFormat minuteDf = new SimpleDateFormat("mm");
        Date now = new Date();
        int hours = Integer.parseInt(hourDf.format(now));
        int minutes = Integer.parseInt(minuteDf.format(now));
        ArticleListPageProcessor listPageProcessor = (ArticleListPageProcessor)processor;
        //如果是整点4,8,12,16,20,0
        if (hours % 4 == 0 && minutes == 0) {
            listPageProcessor.setLimitPageNumber(-1);
            logger.info("start 全量爬取 taskName=" + taskName);
        } else if (hours % 4 == 0 && minutes == 15) {
            logger.info("在全量爬取的时间点上,15分钟次不执行");
            return;
        } else {
            listPageProcessor.setLimitPageNumber(limitPageNumber);
            logger.info("start 热点数据爬取 taskName=" + taskName + ",limitPageNumber=" + limitPageNumber);
        }
        if (needCookie) {
            this.setCookie(listPageProcessor.getSite());
        }
        Spider voteSpider = Spider.create(processor)
                .addUrl(url)
                .addPipeline(articleDbPipeline)
                .thread(threadNum)
                .setDownloader(httpClientDownloader);
        voteSpider.run();
        Integer topicId = ((ArticleListPageProcessor) processor).getTopicId();
        cacheRefreshService.syncDB2RedisByTopic(topicId, SpiderConstant.TOPIC_SHOW_NUM);
    }

}
