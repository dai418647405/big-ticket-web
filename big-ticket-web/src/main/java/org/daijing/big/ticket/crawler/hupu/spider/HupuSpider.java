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
import us.codecraft.webmagic.processor.PageProcessor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Async
    @Override
    public void schedule() {
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
                .thread(threadNum);
        voteSpider.run();
    }
}
