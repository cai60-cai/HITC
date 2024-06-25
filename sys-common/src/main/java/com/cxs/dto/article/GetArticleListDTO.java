package com.cxs.dto.article;

import com.cxs.base.BaseRequest;
import lombok.Data;

import java.util.List;

  
@Data
public class GetArticleListDTO extends BaseRequest {
    private String keyword;
    private List<Integer> tags;
    private Integer typeId;
    private Integer tagId;
}
