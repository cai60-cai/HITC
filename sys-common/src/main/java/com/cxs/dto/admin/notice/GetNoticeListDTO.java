package com.cxs.dto.admin.notice;

import com.cxs.base.BaseRequest;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

  
@Data
public class GetNoticeListDTO extends BaseRequest {
    private String keyword;
    @ParamRangeValid(ranges = {"0", "1"}, message = "isPush范围为{0,1}")
    private Integer isPush;
}
