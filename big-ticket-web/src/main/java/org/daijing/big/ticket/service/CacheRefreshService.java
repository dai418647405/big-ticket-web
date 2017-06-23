package org.daijing.big.ticket.service;

/**
 * Created by daijing03 on 17/6/16.
 */
public interface CacheRefreshService {
    /**
     * 同步db数据到redis
     * @param topicId 话题id
     * @param num 刷前多少条数据
     * @return boolean
     */
    boolean syncDB2RedisByTopic(int topicId, int num);
}
