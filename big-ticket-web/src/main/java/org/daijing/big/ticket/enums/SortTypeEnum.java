package org.daijing.big.ticket.enums;

import lombok.Getter;

/**排序类型枚举
 * Created by daijing03 on 17/5/16.
 */
public enum SortTypeEnum {
    LATEST_REPLY(1, "最新回复"), LATEST_PUBLISH(2, "最新发表")
    , MOST_HOT_TODAY(3, "今天最热"), MOST_HOT_LAST_3DAYS(4, "过去三天最热")
    , MOST_HOT_LAST_7DAYS(5, "过去七天最热"), MOST_HOT_HISTORY(6, "历史最热");

    @Getter
    private int type;
    @Getter
    private String desc;

    SortTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
