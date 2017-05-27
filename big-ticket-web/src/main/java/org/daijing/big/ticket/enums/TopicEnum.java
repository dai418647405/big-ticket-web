package org.daijing.big.ticket.enums;

import lombok.Getter;

/**话题枚举
 * Created by daijing03 on 17/5/16.
 */
public enum TopicEnum {

    SHI_HU_HU(1, "湿乎乎话题区"), WALKING_STREET(2, "步行街"), FOOTBALL(3, "足球区"), MOVIE(4, "影视区"), GAME(5, "电竞区"), ACG(6, "ACG区");

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
            case 2 : return WALKING_STREET;
            case 3 : return FOOTBALL;
            case 4 : return MOVIE;
            case 5 : return GAME;
            case 6 : return ACG;
            default: return SHI_HU_HU;
        }
    }
}
