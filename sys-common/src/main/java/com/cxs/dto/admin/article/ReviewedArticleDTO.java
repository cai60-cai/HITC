package com.cxs.dto.admin.article;

import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import javax.validation.constraints.NotNull;

  
@Data
public class ReviewedArticleDTO {

    @NotNull(message = "文章id为必传项")
    private Integer articleId;
    /**
     * 是否官方推荐
     */
    @NotNull(message = "是否推荐为必传项")
    @ParamRangeValid(ranges = {"0", "1"}, message = "是否推荐仅能传0,1")
    private Integer articleIsRecommend;

    /**
     * 下载文章所需积分
     */
    private Integer articleDownloadPoint;

    /**
     * 文章状态,0、未审核 1、已通过 2、未通过
     */
    @NotNull(message = "文章状态必传项")
    @ParamRangeValid(ranges = {"0", "1", "2"}, message = "文章状态必传项仅能传0,1,2")
    private Integer articleStatus;

    /**
     * 文章评分
     */
    private Float articleRate;

    private String articleDesc;
}
