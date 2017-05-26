package org.daijing.big.ticket.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by daijing03 on 17/5/22.
 */
public class BlogArticleVO {
    @Getter
    @Setter
    private Integer blogId;
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String htmlContent;
    @Getter
    @Setter
    private String otherTypeContent;
    @Getter
    @Setter
    private String author;
    @Getter
    @Setter
    private String tag;
    @Getter
    @Setter
    private Integer type;
    @Getter
    @Setter
    private Integer seq;
    @Getter
    @Setter
    private Date publishTime;

    @Override
    public String toString() {
        return "BlogArticleVO{" +
                "blogId=" + blogId +
                ", title='" + title + '\'' +
                ", htmlContent='" + htmlContent + '\'' +
                ", otherTypeContent='" + otherTypeContent + '\'' +
                ", author='" + author + '\'' +
                ", tag='" + tag + '\'' +
                ", type=" + type +
                ", seq=" + seq +
                ", publishTime=" + publishTime +
                '}';
    }
}
