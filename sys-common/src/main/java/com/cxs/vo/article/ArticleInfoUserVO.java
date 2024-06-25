package com.cxs.vo.article;

import com.cxs.vo.user.UserSettingVO;
import lombok.Data;

@Data
public class ArticleInfoUserVO {

    private String userId;

    private String nickName;

    private String avatar;

    private String autograph;

    private Integer point;

    private Integer articleCount;

    private Long selfArticleCount;

    private Integer likedCount;

    private Integer collectionCount;

    private UserSettingVO settings;
}
