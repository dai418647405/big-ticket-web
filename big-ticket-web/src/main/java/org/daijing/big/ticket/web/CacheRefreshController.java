package org.daijing.big.ticket.web;

import org.daijing.big.ticket.enums.TopicEnum;
import org.daijing.big.ticket.service.CacheRefreshService;
import org.daijing.big.ticket.utils.HupuListPageConstant;
import org.daijing.big.ticket.utils.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by daijing03 on 17/6/28.
 */
@RequestMapping("/cache/refresh")
@Controller
public class CacheRefreshController {

    private static final Logger logger = LoggerFactory.getLogger(CacheRefreshController.class);

    @Qualifier("cacheRefreshServiceImpl")
    @Autowired
    private CacheRefreshService cacheRefreshService;

    @RequestMapping("/topic/list")
    @ResponseBody
    public boolean refreshTopicList(@RequestParam(value = "topicId") Integer topicId,
                                    @RequestParam(value = "token") String token) {
        try {
            if (!ValidateUtil.validatePass(token)) {
                logger.error("刷新缓存验证失败");
                return false;
            }
            return cacheRefreshService.syncDB2RedisByTopic(topicId, HupuListPageConstant.TOPIC_SHOW_NUM);
        } catch (Exception e) {
            logger.error("刷新异常:topId:" + TopicEnum.getTopicEnumById(topicId).getDesc(), e);
            return false;
        }
    }
}
