package com.cxs.bo;

import com.cxs.model.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

  
@Data
public class UserArticleListBo {

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

    private Float articleRate;

    /**
     * 文章分类名称
     */
    private String typeName;

    private String userId;

    private String nickName;


    private LocalDateTime createTime;

    private List<Tag> tagList;
}
