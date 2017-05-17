package org.daijing.big.ticket.dao.hupu.mapper;

import org.apache.ibatis.annotations.Param;
import org.daijing.big.ticket.dao.hupu.po.VoteArticlePO;

import java.util.List;

/**
 * Created by daijing03 on 17/5/9.
 */
public interface VoteArticleMapper {
    void add();

    void batchAdd(@Param("list")List<VoteArticlePO> list);

    void addPublishTime(@Param("po") VoteArticlePO po);

    void batchAddPublishTime(@Param("list")List<VoteArticlePO> list);

    int getVoteArticleListTotalNum();

    List<VoteArticlePO> getVoteArticleListByPageAndSort(@Param("begin") int begin, @Param("offset") int offset, @Param("sortType") int sortType);
}
