package com.cxs.dto;

import com.cxs.base.BaseRequest;
import lombok.Data;


@Data
public class KeyWordSearchPageDTO extends BaseRequest {
    private String keyWord;
}
