package org.daijing.big.ticket.service.impl;

import org.daijing.big.ticket.dao.hupu.mapper.VoteArticleMapper;
import org.daijing.big.ticket.dao.hupu.po.VoteArticlePO;
import org.daijing.big.ticket.enums.TopicEnum;
import org.daijing.big.ticket.service.HupuListPageService;
import org.daijing.big.ticket.vo.PageModelVO;
import org.daijing.big.ticket.vo.PaginationVO;
import org.daijing.big.ticket.vo.VoteArticleVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daijing03 on 17/5/16.
 */
@Service("hupuListPageServiceImpl")
public class HupuListPageServiceImpl implements HupuListPageService {

    private static final Logger logger = LoggerFactory.getLogger(HupuListPageServiceImpl.class);

    @Autowired
    private VoteArticleMapper voteArticleMapper;

    @Override
    public PageModelVO<VoteArticleVO> getListPage(Integer topicId, Integer sortType, PaginationVO pager) {
        if (topicId == null || sortType == null || pager == null
                || pager.getCurrent() == null || pager.getPageSize() == null || pager.getTotal() == null) {
            throw new IllegalArgumentException("必填参数不能为空");
        }
        TopicEnum topicEnum = TopicEnum.getTopicEnumById(topicId);
        switch (topicEnum) {
            case SHI_HU_HU : return this.getShiHuHuPostList(sortType, pager);
            default : return this.getShiHuHuPostList(sortType, pager);
        }
    }

    private PageModelVO<VoteArticleVO> getShiHuHuPostList(Integer sortType, PaginationVO pager) {
        PageModelVO<VoteArticleVO> pageModelVO = new PageModelVO<VoteArticleVO>();
        //如果是第一页,赋总数
        if (pager.getCurrent() == 1) {
            pager.setTotal(voteArticleMapper.getVoteArticleListTotalNum());
        }
        //分页查询
        int begin = this.getBegin(pager.getCurrent(), pager.getPageSize());
        List<VoteArticlePO> poList = voteArticleMapper.getVoteArticleListByPageAndSort(begin, pager.getPageSize(), sortType);
        List<VoteArticleVO> voList = new ArrayList<VoteArticleVO>(poList.size());
        VoteArticleVO vo;
        for (VoteArticlePO po : poList) {
            vo = new VoteArticleVO();
            BeanUtils.copyProperties(po, vo);
            voList.add(vo);
        }
        //封装返回
        pageModelVO.setDataList(voList);
        pageModelVO.setPager(pager);
        return pageModelVO;
    }

    private int getBegin(int current, int pageSize) {
        return (current - 1) * pageSize;
    }
}
