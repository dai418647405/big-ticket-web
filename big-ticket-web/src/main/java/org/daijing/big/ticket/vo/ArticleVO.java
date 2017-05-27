package org.daijing.big.ticket.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**湿乎乎话题列表记录
 * Created by daijing03 on 17/5/16.
 */
public class ArticleVO {
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

    @Override
    public String toString() {
        return "ArticleVO{" +
                "articleId=" + articleId +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", replyCount=" + replyCount +
                ", pageViewCount=" + pageViewCount +
                ", publishTime=" + publishTime +
                ", lastReplyTime=" + lastReplyTime +
                '}';
    }
}
