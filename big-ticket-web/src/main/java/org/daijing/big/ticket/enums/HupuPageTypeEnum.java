package org.daijing.big.ticket.enums;

import lombok.Getter;

/**
 * Created by daijing03 on 17/5/15.
 */
public enum HupuPageTypeEnum {

    SHI_HU_HU_LIST_PAGE(1, "湿乎乎话题"), POST_PAGE(2, "文章详情页");

    @Getter
    private int type;

    @Getter
    private String desc;

    HupuPageTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
