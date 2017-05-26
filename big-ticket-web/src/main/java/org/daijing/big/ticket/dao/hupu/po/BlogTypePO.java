package org.daijing.big.ticket.dao.hupu.po;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by daijing03 on 17/5/22.
 */
public class BlogTypePO extends BasePO {
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
        return "BlogTypePO{" +
                "typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", seq=" + seq +
                '}';
    }
}
