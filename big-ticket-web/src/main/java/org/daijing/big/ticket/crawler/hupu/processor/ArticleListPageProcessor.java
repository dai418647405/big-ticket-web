package org.daijing.big.ticket.crawler.hupu.processor;

import lombok.Getter;
import lombok.Setter;
import org.daijing.big.ticket.enums.HupuPageTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.daijing.big.ticket.utils.SpiderConstant.*;

/**
 * dj
 */
public class ArticleListPageProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(ArticleListPageProcessor.class);

    public static final String postPageUrl = "https?://bbs.hupu.com/\\d+.html";
    public static final String number = "\\d+";
    public static final String postfix = "/\\d+.html";
    public static final String listPageUrl="https?:\\/\\/bbs\\.hupu\\.com\\/\\w*";
    public static final String domain = "http://bbs.hupu.com";


    //帖子页正则
    private Pattern postPagePattern;
    //数字正则
    private Pattern numberPattern;
    //帖子页不包含域名正则
    private Pattern postfixPattern;
    //列表首页正则 https://bbs.hupu.com/vote
    private Pattern listHeadPagePattern;

    @Getter
    @Setter
    private String topicName;

    @Getter
    @Setter
    private Integer topicId;

    @Getter
    @Setter
    private Integer limitPageNumber;

    public ArticleListPageProcessor() {
        postPagePattern = Pattern.compile(postPageUrl);
        numberPattern = Pattern.compile(number);
        postfixPattern = Pattern.compile(postfix);
        listHeadPagePattern = Pattern.compile(listPageUrl);
    }

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me()
            .setDomain("bbs.hupu.com")
            .setCycleRetryTimes(DOWNLOAD_FAILED_CYCLE_RETRY_TIMES)
            .setRetryTimes(HTTP_CLIENT_CONN_TIMEOUT_RETRY_TIMES)
            .setSleepTime(CRAWL_TIME_INTERVAL)
            .setTimeOut(HTTP_SOCKET_TIME_OUT)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
            .addHeader("Accept-Encoding", "gzip, deflate, sdch, br")
            .addHeader("Authority", "bbs.hupu.com")
            .setCharset("UTF-8");

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
                return;
            }
            page.putField("id", page.getUrl().regex("\\d+").get());
            page.putField("publishTime", publishTime);
            page.putField("pageType", HupuPageTypeEnum.POST_PAGE.getType());
            page.putField("topicId", topicId);
            return;
        }
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
//        List<String> idList = html.css("#pl tr", "mid").regex("\\d+").all();
//        List<String> titleList = html.css("#pl .p_title a[id=\"\"]", "innerHtml").replace("<[^>]+>", "").all();
//        List<String> hrefList = html.css("#pl .p_title a[id=\"\"]", "href").all();
//        List<String> replyAndPageViewCountList = html.css("tr .p_re", "text").all();
//        List<String> lastReplyTimeList = html.css(".p_retime a", "text").all();
        List<String> idList = html.css(".titlelink a", "href").regex("\\/(\\d+)\\.html").all();
        List<String> titleList = html.css(".titlelink a").regex("<a.*href=\"\\/\\d+\\.html.*<\\/a>").replace("<[^>]+>", "").all();
        List<String> hrefList = html.css(".titlelink a", "href").regex("\\/\\d+\\.html").all();
        List<String> replyAndPageViewCountList = html.css(".ansour", "text").all();
        List<String> lastReplyTimeList = html.css(".endreply a", "text").all();
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
            for (int index = 0; index <= hrefList.size() - 1; index ++) {
                if (postfixPattern.matcher(hrefList.get(index)).matches()) {
                    hrefList.set(index, domain + hrefList.get(index));
                }
            }
            page.putField("replyCountList", replyCountList);
            page.putField("pageViewCountList", pageViewCountList);
            page.putField("lastReplyTimeList", lastReplyTimeList);
            page.putField("pageType", HupuPageTypeEnum.LIST_PAGE.getType());
            page.putField("topicId", topicId);
        }

        // 部分三：从页面发现后续的url地址来抓取
        //列表页
        List<String> postListUrls = new ArrayList<String>(0);
        Matcher listHeadPageMatcher = listHeadPagePattern.matcher(page.getUrl().toString());
        if (listHeadPageMatcher.matches()) {
            for (int pageNumber = 2; pageNumber <= 10; pageNumber ++) {
                postListUrls.add(page.getUrl().toString() + "-" + pageNumber);
            }
        }
        if (limitPageNumber > 0) {
            postListUrls = getLimitedPageUrls(postListUrls);
        }
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

    public List<String> getLimitedPageUrls(List<String> urls) {
        if (urls == null || urls.isEmpty()) {
            return new ArrayList<String>(0);
        }
        List<String> limitedPageUrls = new ArrayList<String>(urls.size());
        int pageNumber;
        String pageNumberStr = null;
        for (String url : urls) {
            Matcher matcher = numberPattern.matcher(url);
            if (matcher.find()) {
                pageNumberStr = matcher.group();
            }
            pageNumber = Integer.parseInt(pageNumberStr != null && !pageNumberStr.equals("") ? pageNumberStr : "0");
            if (pageNumber <= limitPageNumber) {
                limitedPageUrls.add(url);
            }
        }
        return limitedPageUrls;
    }

}
