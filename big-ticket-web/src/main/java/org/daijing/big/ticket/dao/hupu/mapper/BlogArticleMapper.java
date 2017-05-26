package org.daijing.big.ticket.dao.hupu.mapper;

import org.apache.ibatis.annotations.Param;
import org.daijing.big.ticket.dao.hupu.po.BlogArticlePO;

import java.util.List;

/**
 * Created by daijing03 on 17/5/22.
 */
public interface BlogArticleMapper {
    int add(BlogArticlePO po);

    void update(@Param("po") BlogArticlePO po);

    BlogArticlePO getBlogByBlogId(@Param("blogId") Integer blogId);

    List<BlogArticlePO> getAllIdAndTitle();
}
