package org.daijing.big.ticket.service.impl;

import com.dianping.squirrel.client.StoreKey;
import org.daijing.big.ticket.dao.hupu.mapper.ArticleMapper;
import org.daijing.big.ticket.enums.SortTypeEnum;
import org.daijing.big.ticket.enums.TopicEnum;
import org.daijing.big.ticket.service.CacheRefreshService;
import org.daijing.big.ticket.utils.ArticleMapperFactory;
import org.daijing.big.ticket.utils.RedisStoreHelper;
import org.daijing.big.ticket.utils.StoreCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**缓存刷新
 * Created by daijing03 on 17/6/16.
 */
@Service("cacheRefreshServiceImpl")
public class CacheRefreshServiceImpl implements CacheRefreshService {

    private static final Logger logger = LoggerFactory.getLogger(CacheRefreshService.class);

    @Qualifier("articleMapperFactory")
    @Autowired
    private ArticleMapperFactory articleMapperFactory;

    private static final int cacheFailedRetryTimes = 3;

    @Override
    public boolean syncDB2RedisByTopic(int topicId, int num) {
        logger.info("开始刷新缓存:" + TopicEnum.getTopicEnumById(topicId).getDesc());
        ArticleMapper articleMapper = articleMapperFactory.getArticleMapperByTopicId(topicId);
        if (articleMapper == null) {
            return false;
        }
        List<Integer> sortTypeList = SortTypeEnum.getAllSortType();
        for (Integer sortType : sortTypeList) {
            int times = 1;
            while(!RedisStoreHelper.setList(new StoreKey(StoreCategory.ARTICLE_LIST_PAGE, topicId, sortType), articleMapper.getListByPageAndSort(0, num, sortType))
                    && times <= cacheFailedRetryTimes) {
                if (times == cacheFailedRetryTimes) {
                    logger.error(String.format("缓存刷新失败%d次, 缓存失败重试已达上限, 刷新缓存失败!", times));
                    return false;
                }
                logger.error(String.format("缓存刷新失败%d次, 重试...", times));
                times ++;
            }
        }
        return true;
    }
}
