package com.cxs.dto.admin.log;

import com.alibaba.fastjson.annotation.JSONField;
import com.cxs.base.BaseRequest;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class GetLogListDTO extends BaseRequest {
    /**
     * 操作结果 1、成功 2、失败
     */
    @ParamRangeValid(ranges = {"1", "2"}, message = "operaResult只能传递{1,2}")
    private Integer operaResult;

    private String keyword;

    /**
     * 操作时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
