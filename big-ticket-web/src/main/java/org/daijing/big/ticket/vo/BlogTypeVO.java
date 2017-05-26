package org.daijing.big.ticket.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by daijing03 on 17/5/22.
 */
public class BlogTypeVO {
    @Getter
    @Setter
    private Integer typeId;
    @Getter
    @Setter
    private String typeName;
    @Getter
    @Setter
    private Integer seq;

    @Override
    public String toString() {
        return "BlogTypeVO{" +
                "typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", seq=" + seq +
                '}';
    }
}
