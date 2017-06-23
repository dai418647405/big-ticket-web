package org.daijing.big.ticket.utils;

import org.daijing.big.ticket.dao.hupu.mapper.ArticleMapper;
import org.daijing.big.ticket.dao.hupu.mapper.*;
import org.daijing.big.ticket.enums.TopicEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**话题表工厂类
 * Created by daijing03 on 17/6/16.
 */
public class ArticleMapperFactory {

    private static final Logger logger = LoggerFactory.getLogger(ArticleMapperFactory.class);

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

    public ArticleMapper getArticleMapperByTopicId(Integer topicId) {
        TopicEnum topicEnum = TopicEnum.getTopicEnumById(topicId);
        switch (topicEnum) {
            case SHI_HU_HU:
                return voteArticleMapper;
            case WALKING_STREET:
                return walkingStreetArticleMapper;
            case FOOTBALL:
                return footBallArticleMapper;
            case MOVIE:
                return movieArticleMapper;
            case GAME:
                return gameArticleMapper;
            case ACG:
                return acgArticleMapper;
            default:
                logger.error("无效的topicId:" + topicId); return null;
        }
    }
}
