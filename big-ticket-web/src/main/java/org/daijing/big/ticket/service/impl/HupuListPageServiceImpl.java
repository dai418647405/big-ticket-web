package org.daijing.big.ticket.service.impl;

import org.daijing.big.ticket.dao.hupu.mapper.*;
import org.daijing.big.ticket.dao.hupu.po.ListRecordPO;
import org.daijing.big.ticket.enums.TopicEnum;
import org.daijing.big.ticket.service.HupuListPageService;
import org.daijing.big.ticket.vo.ArticleVO;
import org.daijing.big.ticket.vo.PageModelVO;
import org.daijing.big.ticket.vo.PaginationVO;
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

    @Override
    public PageModelVO<ArticleVO> getListPage(Integer topicId, Integer sortType, PaginationVO pager) {
        if (topicId == null || sortType == null || pager == null
                || pager.getCurrent() == null || pager.getPageSize() == null || pager.getTotal() == null) {
            throw new IllegalArgumentException("必填参数不能为空");
        }
        PageModelVO<ArticleVO> pageModelVO = new PageModelVO<ArticleVO>();
        //如果是第一页,赋总数
        if (pager.getCurrent() == 1) {
            pager.setTotal(this.getListTotalNum(topicId));
        }
        //分页查询
        int begin = this.getBegin(pager.getCurrent(), pager.getPageSize());
        List<ListRecordPO> poList = this.getListByPageAndSort(topicId, begin, pager.getPageSize(), sortType);
        List<ArticleVO> voList = new ArrayList<ArticleVO>(poList.size());
        ArticleVO vo;
        for (ListRecordPO po : poList) {
            vo = new ArticleVO();
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

    private int getListTotalNum(int topicId) {
        TopicEnum topicEnum = TopicEnum.getTopicEnumById(topicId);
        switch (topicEnum) {
            case SHI_HU_HU : return voteArticleMapper.getListTotalNum();
            case WALKING_STREET : return walkingStreetArticleMapper.getListTotalNum();
            case FOOTBALL : return footBallArticleMapper.getListTotalNum();
            case MOVIE : return movieArticleMapper.getListTotalNum();
            case GAME : return gameArticleMapper.getListTotalNum();
            case ACG : return acgArticleMapper.getListTotalNum();
            default : return 200;
        }
    }

    private List<ListRecordPO> getListByPageAndSort(int topicId, int begin, int offset, int sortType) {
        TopicEnum topicEnum = TopicEnum.getTopicEnumById(topicId);
        switch (topicEnum) {
            case SHI_HU_HU : return voteArticleMapper.getListByPageAndSort(begin, offset, sortType);
            case WALKING_STREET : return walkingStreetArticleMapper.getListByPageAndSort(begin, offset, sortType);
            case FOOTBALL : return footBallArticleMapper.getListByPageAndSort(begin, offset, sortType);
            case MOVIE : return movieArticleMapper.getListByPageAndSort(begin, offset, sortType);
            case GAME : return gameArticleMapper.getListByPageAndSort(begin, offset, sortType);
            case ACG : return acgArticleMapper.getListByPageAndSort(begin, offset, sortType);
            default : return new ArrayList<ListRecordPO>(0);
        }
    }



}
