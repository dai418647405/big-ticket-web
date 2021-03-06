package org.daijing.big.ticket.dao.hupu.mapper;

import org.apache.ibatis.annotations.Param;
import org.daijing.big.ticket.dao.hupu.po.ListRecordPO;

import java.util.List;

/**
 * Created by daijing03 on 17/6/16.
 */
public interface ArticleMapper {
    void batchAdd(@Param("list")List<ListRecordPO> list);

    void addPublishTime(@Param("po") ListRecordPO po);

    void batchAddPublishTime(@Param("list")List<ListRecordPO> list);

    int getListTotalNum();

    List<ListRecordPO> getListByPageAndSort(@Param("begin") int begin, @Param("offset") int offset, @Param("sortType") int sortType);
}
