package org.daijing.big.ticket.vo;

import lombok.Getter;
import lombok.Setter;

/**分页VO
 * Created by daijing03 on 17/5/16.
 */
public class PaginationVO {
    @Getter
    @Setter
    private Integer current;
    @Getter
    @Setter
    private Integer pageSize;
    @Getter
    @Setter
    private Integer total;

    @Override
    public String toString() {
        return "PaginationVO{" +
                "current=" + current +
                ", pageSize=" + pageSize +
                ", total=" + total +
                '}';
    }
}
