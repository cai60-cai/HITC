package com.cxs.dto.tag;

import lombok.Data;

  
@Data
public class TagDTO {
    private Integer tagId;

    /**
     * 标签名
     */
    private String tagName;
}
