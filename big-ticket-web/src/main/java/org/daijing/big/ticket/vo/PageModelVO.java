package org.daijing.big.ticket.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分页返回
 */
public class PageModelVO<T> {

    //分页
    @Getter
    @Setter
    private PaginationVO pager;

    // 当页数据
    @Getter
    @Setter
    private List<T> dataList;

    @Override
    public String toString() {
        return "PageModelVO{" +
                "pager=" + pager +
                ", dataList=" + dataList +
                '}';
    }
}
