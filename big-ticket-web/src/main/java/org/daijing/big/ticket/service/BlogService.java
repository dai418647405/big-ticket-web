package org.daijing.big.ticket.service;

import org.daijing.big.ticket.vo.BlogArticleVO;
import org.daijing.big.ticket.vo.BlogTypeVO;

import java.util.List;

/**
 * Created by daijing03 on 17/5/22.
 */
public interface BlogService {

    List<BlogArticleVO> getBlogTitleList();

    List<BlogTypeVO> getBlogTypeList();

    BlogArticleVO getBlogById(Integer blogId);

    /**
     * 添加博客并返回id
     * @param blog blog
     * @return id 添加失败
     */
    int addBlog(BlogArticleVO blog, String token);
}
