package com.cxs.dto.admin.log;

import com.cxs.base.BaseRequest;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import java.time.LocalDateTime;

import java.util.List;


@Data
public class GetLogListReqDTO extends BaseRequest {
    /**
     * 操作结果 1、成功 2、失败
     */
    @ParamRangeValid(ranges = {"1", "2"}, message = "operaResult只能传递{1,2}")
    private Integer operaResult;

    private String keyword;

    /**
     * 操作时间
     */
    private List<LocalDateTime> timeRange;

}
