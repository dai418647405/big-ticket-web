package org.daijing.big.ticket.crawler.hupu;

import org.daijing.big.ticket.enums.HupuPageTypeEnum;
import org.daijing.big.ticket.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * dj
 */
public class ArticleListPageProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(ArticleListPageProcessor.class);

    public static final String postPageUrl = "https://bbs.hupu.com/\\d+.html";
    private Pattern postPagePattern;


    public ArticleListPageProcessor() {
        postPagePattern = Pattern.compile(postPageUrl);
    }

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
            .addHeader("Accept-Encoding", "gzip, deflate, sdch, br")
            .addHeader("Authority", "bbs.hupu.com")
            .setCharset("UTF-8");
//            .setHttpProxyPool(Lists.newArrayList(
//                    new String[]{"", "", "110.73.38.108", "8123"},
//                    new String[]{"", "", "119.5.1.38", "808"},
//                    new String[]{"", "", "218.92.209.52", "8080"},
//                    new String[]{"", "", "124.89.2.182", "80"},
//                    new String[]{"", "", "115.230.12.54", "808"},
//                    new String[]{"", "", "211.75.64.114", "80"},
//                    new String[]{"", "", "60.21.206.165", "9999"},
//                    new String[]{"", "", "27.38.96.209", "9797"},
//                    new String[]{"", "", "43.226.162.35", "8080"},
//                    new String[]{"", "", "222.94.145.5", "808"},
//                    new String[]{"", "", "124.130.94.193", "8998"},
//                    new String[]{"", "", "182.246.104.29", "8998"},
//                    new String[]{"", "", "211.75.64.156", "80"},
//                    new String[]{"", "", "117.135.164.170", "80"},
//                    new String[]{"", "", "121.18.230.45", "8088"},
//                    new String[]{"", "", "115.213.255.102", "808"},
//                    new String[]{"", "", "42.157.7.43", "9999"},
//                    new String[]{"", "", "121.31.23.46", "80"},
//                    new String[]{"", "", "112.195.66.204", "8118"},
//                    new String[]{"", "", "121.23.20.227", "8998"},
//                    new String[]{"", "", "115.230.11.198", "808"},
//                    new String[]{"", "", "61.132.238.91", "9999"},
//                    new String[]{"", "", "111.23.10.112", "8088"},
//                    new String[]{"", "", "220.189.249", "80"},
//                    new String[]{"", "", "120.199.190.77", "8998"},
//                    new String[]{"", "", "111.40.84.73", "9797"},
//                    new String[]{"", "", "111.11.29.210", "808"},
//                    new String[]{"", "", "125.122.90.220", "808"},
//                    new String[]{"", "", "218.4.242.102", "80"},
//                    new String[]{"", "", "175.1.192.26", "8998"},
//                    new String[]{"", "", "121.17.123.103", "8080"},
//                    new String[]{"", "", "113.123.127.164", "808"},
//                    new String[]{"", "", "110.73.8.5", "8123"}
// ), false);
    @Override
    public void process(Page page) {
        //详情页处理逻辑
         Matcher postPageMatcher = postPagePattern.matcher(page.getUrl().toString());
        if (postPageMatcher.matches()) {
            //判断帖子是否还存在
            String publishTime = page.getHtml().css(".author .left .stime", "text").get();
            if (publishTime == null || publishTime.trim().equals("")) {
                page.setSkip(true);
                logger.error("not found publish time, url=" + page.getUrl());
            }
            page.putField("id", page.getUrl().regex("\\d+").get());
            page.putField("publishTime", publishTime);
            page.putField("type", HupuPageTypeEnum.POST_PAGE.getType());
            return;
        }
        logger.error("+1");
        //获取是否跳过该页面标志位
        ResultItems resultItems;
        try {
            Field field = Page.class.getDeclaredField("resultItems");
            field.setAccessible(true);
            resultItems = (ResultItems)field.get(page);
        } catch (Exception e) {
            logger.error("获取是否跳过该页面标志位失败", e);
            return;
        }
        // 部分二：定义如何抽取页面信息，并保存下来
        Html html = page.getHtml();
        List<String> idList = html.css("#pl tr", "mid").regex("\\d+").all();
        List<String> titleList = html.css("#pl .p_title a[id=\"\"]", "innerHtml").replace("<[^>]+>", "").all();
        List<String> hrefList = html.css("#pl .p_title a[id=\"\"]", "href").all();
        List<String> replyAndPageViewCountList = html.css("tr .p_re", "text").all();
        List<String> lastReplyTimeList = html.css(".p_retime a", "text").all();
        if (titleList == null || titleList.isEmpty()) {
            //skip this page
            page.setSkip(true);
        }
        if (!(titleList != null && hrefList != null && replyAndPageViewCountList != null
                && (titleList.size() == hrefList.size() && hrefList.size() == replyAndPageViewCountList.size()))) {
            page.setSkip(true);
        }

        List<String> replyCountList = new ArrayList<String>(titleList != null ? titleList.size() : 0);
        List<String> pageViewCountList = new ArrayList<String>(titleList != null ? titleList.size() : 0);
        if (replyAndPageViewCountList != null) {
            String[] lightAndReplyCountArr;
            for (String str : replyAndPageViewCountList) {
                lightAndReplyCountArr = str.split("\\D+");
                if (lightAndReplyCountArr.length != 2) {
                    page.setSkip(true);
                    break;
                }
                replyCountList.add(lightAndReplyCountArr[0]);
                pageViewCountList.add(lightAndReplyCountArr[1]);
            }
        }

        if (!resultItems.isSkip()) {
            page.putField("idList", idList);
            page.putField("titleList", titleList);
            page.putField("hrefList", hrefList);
            page.putField("replyCountList", replyCountList);
            page.putField("pageViewCountList", pageViewCountList);
            page.putField("lastReplyTimeList", lastReplyTimeList);
            page.putField("type", HupuPageTypeEnum.SHI_HU_HU_LIST_PAGE.getType());
        }

        // 部分三：从页面发现后续的url地址来抓取
        //列表页
        List<String> postListUrls = page.getHtml().css("div.page").links().regex(".*/vote(-\\d)?.*", 0).all();
        page.addTargetRequests(postListUrls);
        //详情页
        if (hrefList != null && !hrefList.isEmpty()) {
            page.addTargetRequests(hrefList);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }


    public void schedule() {
        Spider voteSpider = Spider.create(new ArticleListPageProcessor())
                .addUrl("https://bbs.hupu.com/vote")
//                .addPipeline(new ConsolePipeline())
//                .addPipeline(new HupuListPagePipeLine())
                .addPipeline((Pipeline) SpringContextUtil.getBean("articleDbPipeline"))
                .thread(20);
        voteSpider.run();
    }
}
