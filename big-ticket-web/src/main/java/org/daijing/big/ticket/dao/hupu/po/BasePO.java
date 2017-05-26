package org.daijing.big.ticket.dao.hupu.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**基础po
 * Created by daijing03 on 17/5/22.
 */
public class BasePO {
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private Date gmtCreate;
    @Getter
    @Setter
    private Date gmtModified;

    @Override
    public String toString() {
        return "BasePO{" +
                "id=" + id +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}
