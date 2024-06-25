package com.cxs.vo.article;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserArticleListVO {
    private Integer articleId;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章摘要
     */
    private String articleAbstract;

    /**
     * 文章封面
     */
    private String articleCover;

    private Integer typeId;
    /**
     * 文章分类名称
     */
    private String typeName;

    private Boolean readFlag;

    private Integer readNum;

    private Float articleRate;

    private String userId;

    private String nickName;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String createTimeFormat;

}
