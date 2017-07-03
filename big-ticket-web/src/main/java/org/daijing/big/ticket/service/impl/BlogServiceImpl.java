package org.daijing.big.ticket.service.impl;

import org.daijing.big.ticket.dao.hupu.mapper.BlogArticleMapper;
import org.daijing.big.ticket.dao.hupu.mapper.BlogTypeMapper;
import org.daijing.big.ticket.dao.hupu.po.BlogArticlePO;
import org.daijing.big.ticket.dao.hupu.po.BlogTypePO;
import org.daijing.big.ticket.service.BlogService;
import org.daijing.big.ticket.utils.BlogConstant;
import org.daijing.big.ticket.utils.ValidateUtil;
import org.daijing.big.ticket.vo.BlogArticleVO;
import org.daijing.big.ticket.vo.BlogTypeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daijing03 on 17/5/22.
 */
@Service("blogServiceImpl")
public class BlogServiceImpl implements BlogService {

    private Logger logger = LoggerFactory.getLogger(BlogService.class);

    @Autowired
    private BlogArticleMapper blogArticleMapper;

    @Autowired
    private BlogTypeMapper blogTypeMapper;


    @Override
    public List<BlogArticleVO> getBlogTitleList() {
        List<BlogArticlePO> poList = blogArticleMapper.getAllIdAndTitle();
        List<BlogArticleVO> voList = new ArrayList<BlogArticleVO>(poList.size());
        BlogArticleVO vo;
        for (BlogArticlePO po : poList) {
            vo = new BlogArticleVO();
            BeanUtils.copyProperties(po, vo);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public List<BlogTypeVO> getBlogTypeList() {
        List<BlogTypePO> poList = blogTypeMapper.getAll();
        List<BlogTypeVO> voList = new ArrayList<BlogTypeVO>(poList.size());
        BlogTypeVO vo;
        for (BlogTypePO po : poList) {
            vo = new BlogTypeVO();
            BeanUtils.copyProperties(po, vo);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public BlogArticleVO getBlogById(Integer blogId) {
        if (blogId == null) {
            return null;
        }
        BlogArticleVO res = null;
        BlogArticlePO po = blogArticleMapper.getBlogByBlogId(blogId);
        if (po != null) {
            res = new BlogArticleVO();
            BeanUtils.copyProperties(po, res);
        }
        return res;
    }


    @Transactional
    @Override
    public int addBlog(BlogArticleVO blog, String token) {
        if (!ValidateUtil.validatePass(token)) {
            return -1;
        }
        if (blog == null) {
            return -1;
        }
        BlogArticlePO po = new BlogArticlePO();
        BeanUtils.copyProperties(blog, po);
        if (po.getBlogId() == BlogConstant.CREATE_NEW) {
            blogArticleMapper.add(po);
            //blogId为自增id
            po.setBlogId(po.getId());
            blogArticleMapper.update(po);
        } else {
            //修改
            blogArticleMapper.add(po);
        }
        return po.getBlogId();
    }
}
