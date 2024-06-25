package com.cxs.dto.admin.point;

import com.cxs.base.BaseRequest;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

  
@Data
public class GetPointTradeOrderListDTO extends BaseRequest {

    @ParamRangeValid(ranges = {"0", "1", "2"}, message = "orderStstus仅能传递0,1,2")
    private Integer orderStstus;
    private String orderUser;
}
