package org.daijing.big.ticket.crawler.hupu;

import org.apache.http.annotation.ThreadSafe;
import org.daijing.big.ticket.dao.hupu.mapper.VoteArticleMapper;
import org.daijing.big.ticket.dao.hupu.po.VoteArticlePO;
import org.daijing.big.ticket.enums.HupuPageTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

/**
 * Created by daijing03 on 17/5/9.
 */
@Qualifier("articleDbPipeline")
@Component
//@Scope("prototype")
@ThreadSafe
public class ArticleDbPipeline implements Pipeline {

    @Autowired
    private VoteArticleMapper voteArticleMapper;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void process(ResultItems resultItems, Task task) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //如果是帖子详情页
        Integer type = resultItems.get("type");
        if (type.equals(HupuPageTypeEnum.POST_PAGE.getType())) {
            Integer articleId = Integer.parseInt(resultItems.get("id").toString());
            Date publishTime;
            try {
                publishTime = sdf.parse(resultItems.get("publishTime").toString() + ":00");
            } catch (ParseException e) {
                logger.error("解析发表时间异常, articleId=" + articleId, e);
                return;
            }
            VoteArticlePO po = new VoteArticlePO();
            po.setArticleId(articleId);
            po.setPublishTime(publishTime);
            voteArticleMapper.addPublishTime(po);
            return;
        }
        List<String> idList = resultItems.get("idList");
        List<String> titleList = resultItems.get("titleList");
        List<String> hrefList = resultItems.get("hrefList");
        List<String> replyCountList = resultItems.get("replyCountList");
        List<String> pageViewCountList = resultItems.get("pageViewCountList");
        List<String> lastReplyTimeList = resultItems.get("lastReplyTimeList");

        List<VoteArticlePO> list = new ArrayList<VoteArticlePO>(idList.size());
        try {
            HupuListPagePipeLine.handleReplyTimeFormat(lastReplyTimeList);
            VoteArticlePO po;
            for (int index = 0; index <= titleList.size() - 1; index ++) {
                po = new VoteArticlePO();
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
        voteArticleMapper.batchAdd(list);
    }
}
