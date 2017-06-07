package org.daijing.big.ticket.crawler.hupu.spider;

import lombok.Getter;
import lombok.Setter;
import org.daijing.big.ticket.crawler.hupu.SpiderScheduler;
import org.daijing.big.ticket.crawler.hupu.pipeline.ArticleDbPipeline;
import org.daijing.big.ticket.crawler.hupu.processor.ArticleListPageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
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
    private PageProcessor processor;

    @Qualifier("articleDbPipeline")
    @Autowired
    private ArticleDbPipeline articleDbPipeline;

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

    @Async
    @Override
    public void schedule() {
        if (!permitCrawl()) {
            logger.error("禁止爬取!");
            return;
        }
        this.setProxyProvider();
        DateFormat df = new SimpleDateFormat("mm");
        int minutes = Integer.parseInt(df.format(new Date()));
        ArticleListPageProcessor listPageProcessor = (ArticleListPageProcessor)processor;
        if (minutes == 0) {
            listPageProcessor.setLimitPageNumber(-1);
            logger.info("start 全量爬取 taskName=" + taskName);
        } else {
            listPageProcessor.setLimitPageNumber(limitPageNumber);
            logger.info("start 热点数据爬取 taskName=" + taskName + ",limitPageNumber=" + limitPageNumber);
        }
        Spider voteSpider = Spider.create(processor)
                .addUrl(url)
                .addPipeline(articleDbPipeline)
                .thread(threadNum)
                .setDownloader(httpClientDownloader);
        voteSpider.run();
    }

}
