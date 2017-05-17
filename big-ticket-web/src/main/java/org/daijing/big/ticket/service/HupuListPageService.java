package org.daijing.big.ticket.service;

import org.daijing.big.ticket.vo.PageModelVO;
import org.daijing.big.ticket.vo.PaginationVO;
import org.daijing.big.ticket.vo.VoteArticleVO;

/**
 * Created by daijing03 on 17/5/16.
 */
public interface HupuListPageService {
    PageModelVO<VoteArticleVO> getListPage(Integer topicId, Integer sortType, PaginationVO pager);
}
