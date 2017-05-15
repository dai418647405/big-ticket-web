package org.daijing.big.ticket.dao.hupu.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by daijing03 on 17/5/9.
 */
public class VoteArticlePO {
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private Integer articleId;
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String link;
    @Getter
    @Setter
    private Integer replyCount;
    @Getter
    @Setter
    private Integer pageViewCount;
    @Getter
    @Setter
    private Date publishTime;
    @Getter
    @Setter
    private Date lastReplyTime;
    @Getter
    @Setter
    private Date gmtCreate;

    @Override
    public String toString() {
        return "VoteArticlePO{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", replyCount=" + replyCount +
                ", pageViewCount=" + pageViewCount +
                ", publishTime=" + publishTime +
                ", lastReplyTime=" + lastReplyTime +
                ", gmtCreate=" + gmtCreate +
                '}';
    }
}
