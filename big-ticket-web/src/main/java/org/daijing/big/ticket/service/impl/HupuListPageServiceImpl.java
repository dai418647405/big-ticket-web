package org.daijing.big.ticket.service.impl;

import com.dianping.squirrel.client.StoreKey;
import org.daijing.big.ticket.dao.hupu.mapper.ArticleMapper;
import org.daijing.big.ticket.dao.hupu.po.ListRecordPO;
import org.daijing.big.ticket.service.HupuListPageService;
import org.daijing.big.ticket.utils.ArticleMapperFactory;
import org.daijing.big.ticket.utils.RedisStoreHelper;
import org.daijing.big.ticket.utils.StoreCallBack;
import org.daijing.big.ticket.utils.StoreCategory;
import org.daijing.big.ticket.vo.ArticleVO;
import org.daijing.big.ticket.vo.PageModelVO;
import org.daijing.big.ticket.vo.PaginationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daijing03 on 17/5/16.
 */
@Service("hupuListPageServiceImpl")
public class HupuListPageServiceImpl implements HupuListPageService {

    private static final Logger logger = LoggerFactory.getLogger(HupuListPageServiceImpl.class);

    @Qualifier("articleMapperFactory")
    @Autowired
    private ArticleMapperFactory articleMapperFactory;

    private static final int maxTotalNum = 1500;

    @Override
    public PageModelVO<ArticleVO> getListPage(Integer topicId, Integer sortType, PaginationVO pager) {
        if (topicId == null || sortType == null || pager == null
                || pager.getCurrent() == null || pager.getPageSize() == null || pager.getTotal() == null) {
            throw new IllegalArgumentException("必填参数不能为空");
        }
        PageModelVO<ArticleVO> pageModelVO = new PageModelVO<ArticleVO>();
        //如果是第一页,赋总数
        int listTotalNum;
        if (pager.getCurrent() == 1) {
//            pager.setTotal((listTotalNum = this.getListTotalNum(topicId)) > maxTotalNum ? maxTotalNum : listTotalNum);
            pager.setTotal(maxTotalNum);
        }
        //分页查询
        int begin = this.getBegin(pager.getCurrent(), pager.getPageSize());
        List<ListRecordPO> poList = this.getListByPageAndSort(topicId, begin, pager.getPageSize(), sortType);
        //转换
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
        ArticleMapper articleMapper = articleMapperFactory.getArticleMapperByTopicId(topicId);
        if (articleMapper != null) {
            return articleMapper.getListTotalNum();
        } else {
            return 200;
        }
    }


    private List<ListRecordPO> getListByPageAndSort(final int topicId, final int begin, final int offset, final int sortType) {
        StoreKey cacheKey = new StoreKey(StoreCategory.ARTICLE_LIST_PAGE, topicId, sortType);

        return RedisStoreHelper.getList(cacheKey, new StoreCallBack<List<ListRecordPO>>(){
            @Override
            public List<ListRecordPO> getResult(StoreKey missKey) {
                return getListByPageAndSortFromDB(topicId, begin, offset, sortType);
            }
        }, true, begin, begin + offset - 1);
    }

    private List<ListRecordPO> getListByPageAndSortFromDB(int topicId, int begin, int offset, int sortType) {
        ArticleMapper articleMapper = articleMapperFactory.getArticleMapperByTopicId(topicId);
        if (articleMapper != null) {
            return articleMapper.getListByPageAndSort(begin, offset, sortType);
        } else {
            return new ArrayList<ListRecordPO>(0);
        }
    }

}
