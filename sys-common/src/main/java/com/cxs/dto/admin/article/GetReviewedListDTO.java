package com.cxs.dto.admin.article;

import com.cxs.base.BaseRequest;
import lombok.Data;

  
@Data
public class GetReviewedListDTO extends BaseRequest {
    private String keyWord;

    private Integer articleIsSelf;

    private Integer articleType;
}
