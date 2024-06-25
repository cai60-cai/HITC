package com.cxs.dto.admin.report;

import com.cxs.base.BaseRequest;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;


@Data
public class AdminGetReportListDTO extends BaseRequest {

    @ParamRangeValid(ranges = {"1", "0"}, message = "reportStatus范围不允许")
    private Integer reportStatus;
}
