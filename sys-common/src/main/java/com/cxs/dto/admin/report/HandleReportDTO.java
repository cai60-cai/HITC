package com.cxs.dto.admin.report;

import com.cxs.valid.annotation.ParamLengthValid;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class HandleReportDTO {
    /**
     * 举报id
     */
    @NotNull(message = "reportId为必填项")
    private Integer reportId;

    @NotNull(message = "emailFlag为必填项")
    @ParamRangeValid(ranges = {"0", "1"}, message = "emailFlag只能传0,1")
    private Integer emailFlag;

    /**
     * 处理方式
     */
    @NotNull(message = "handleNo为必填项")
    private String handleNo;

    private Integer banMinute;

    /**
     * 回复内容
     */
    @ParamLengthValid(max = 1000, message = "reportHandleDesc不可大于1000字符")
    private String reportHandleDesc;
}
