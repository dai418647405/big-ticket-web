package org.daijing.big.ticket.enums;

import lombok.Getter;

/**话题枚举
 * Created by daijing03 on 17/5/16.
 */
public enum TopicEnum {

    SHI_HU_HU(1, "湿乎乎话题区");

    @Getter
    private int id;
    @Getter
    private String desc;

    TopicEnum(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public static TopicEnum getTopicEnumById(int id) {
        switch (id) {
            case 1 : return SHI_HU_HU;
            default: return SHI_HU_HU;
        }
    }
}
