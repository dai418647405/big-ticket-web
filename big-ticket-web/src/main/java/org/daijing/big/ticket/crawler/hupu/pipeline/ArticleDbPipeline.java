package org.daijing.big.ticket.crawler.hupu.pipeline;

import org.apache.http.annotation.ThreadSafe;
import org.daijing.big.ticket.dao.hupu.mapper.*;
import org.daijing.big.ticket.dao.hupu.po.ListRecordPO;
import org.daijing.big.ticket.enums.HupuPageTypeEnum;
import org.daijing.big.ticket.enums.TopicEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by daijing03 on 17/5/9.
 */
@Qualifier("articleDbPipeline")
@Component
@Scope("prototype")
@ThreadSafe
public class ArticleDbPipeline implements Pipeline {

    private Logger logger = LoggerFactory.getLogger(ArticleDbPipeline.class);

    @Autowired
    private VoteArticleMapper voteArticleMapper;
    @Autowired
    private AcgArticleMapper acgArticleMapper;
    @Autowired
    private FootBallArticleMapper footBallArticleMapper;
    @Autowired
    private GameArticleMapper gameArticleMapper;
    @Autowired
    private MovieArticleMapper movieArticleMapper;
    @Autowired
    private WalkingStreetArticleMapper walkingStreetArticleMapper;

    private List<ListRecordPO> poWithPublishTimeList = new ArrayList<ListRecordPO>(BATCH_NUM);

    private static final int BATCH_NUM = 100;

    private Lock lock = new ReentrantLock();// 锁对象

    @Override
    public void process(ResultItems resultItems, Task task) {
        //如果是帖子详情页
        Integer pageType = resultItems.get("pageType");
        Integer topicId = resultItems.get("topicId");
        if (pageType.equals(HupuPageTypeEnum.POST_PAGE.getType())) {
            outputPostPageData(resultItems, topicId);
            return;
        }
        outputListPageData(resultItems, topicId);
    }

    private void outputPostPageData(ResultItems resultItems, Integer topicId) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer articleId = Integer.parseInt(resultItems.get("id").toString());
        Date publishTime;
        try {
            publishTime = sdf.parse(resultItems.get("publishTime").toString() + ":00");
        } catch (ParseException e) {
            logger.error("解析发表时间异常, articleId=" + articleId, e);
            return;
        }
        ListRecordPO po = new ListRecordPO();
        po.setArticleId(articleId);
        po.setPublishTime(publishTime);
        //锁代码块
        lock.lock();
        try {
            poWithPublishTimeList.add(po);
            if (poWithPublishTimeList.size() == BATCH_NUM) {
                batchAddPublishTime(topicId);
                poWithPublishTimeList.clear();
            }
        } finally {
            lock.unlock();
        }
    }

    private void outputListPageData(ResultItems resultItems, Integer topicId) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> idList = resultItems.get("idList");
        List<String> titleList = resultItems.get("titleList");
        List<String> hrefList = resultItems.get("hrefList");
        List<String> replyCountList = resultItems.get("replyCountList");
        List<String> pageViewCountList = resultItems.get("pageViewCountList");
        List<String> lastReplyTimeList = resultItems.get("lastReplyTimeList");
        List<ListRecordPO> list = new ArrayList<ListRecordPO>(idList.size());
        try {
            HupuListPagePipeLine.handleReplyTimeFormat(lastReplyTimeList);
            ListRecordPO po;
            for (int index = 0; index <= titleList.size() - 1; index ++) {
                po = new ListRecordPO();
                po.setArticleId(Integer.valueOf(idList.get(index)));
                po.setTitle(titleList.get(index));
                po.setLink(hrefList.get(index));
                po.setReplyCount(Integer.valueOf(replyCountList.get(index)));
                po.setPageViewCount(Integer.valueOf(pageViewCountList.get(index)));
                po.setLastReplyTime(sdf.parse(lastReplyTimeList.get(index)));
                list.add(po);
            }
        } catch (ParseException e) {
            logger.error("解析最后回复时间异常", e);
            return;
        }
        batchAdd(topicId, list);
    }

    private void batchAddPublishTime(Integer topicId) {
        TopicEnum topicEnum = TopicEnum.getTopicEnumById(topicId);
        switch (topicEnum) {
            case SHI_HU_HU : voteArticleMapper.batchAddPublishTime(poWithPublishTimeList); break;
            case WALKING_STREET : walkingStreetArticleMapper.batchAddPublishTime(poWithPublishTimeList); break;
            case FOOTBALL : footBallArticleMapper.batchAddPublishTime(poWithPublishTimeList); break;
            case MOVIE : movieArticleMapper.batchAddPublishTime(poWithPublishTimeList); break;
            case GAME : gameArticleMapper.batchAddPublishTime(poWithPublishTimeList); break;
            case ACG : acgArticleMapper.batchAddPublishTime(poWithPublishTimeList); break;
            default: ;
        }
    }

    private void batchAdd(Integer topicId, List<ListRecordPO> list) {
        TopicEnum topicEnum = TopicEnum.getTopicEnumById(topicId);
        switch (topicEnum) {
            case SHI_HU_HU : voteArticleMapper.batchAdd(list); break;
            case WALKING_STREET : walkingStreetArticleMapper.batchAdd(list); break;
            case FOOTBALL : footBallArticleMapper.batchAdd(list); break;
            case MOVIE : movieArticleMapper.batchAdd(list); break;
            case GAME : gameArticleMapper.batchAdd(list); break;
            case ACG : acgArticleMapper.batchAdd(list); break;
            default: ;
        }
    }

}
