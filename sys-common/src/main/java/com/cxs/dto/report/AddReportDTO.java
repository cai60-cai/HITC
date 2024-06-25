package com.cxs.dto.report;

import com.cxs.valid.annotation.ParamLengthValid;
import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Data
public class AddReportDTO {

    /**
     * 举报类型
     */
    @NotBlank(message = "举报类型为必传项")
    private String reportType;

    /**
     * 举报描述
     */
    @NotBlank(message = "举报描述为必传项")
    @ParamLengthValid(max = 1024, message = "举报描述不超过1024个字符")
    private String reportContent;

    /**
     * 举报图片
     */
    private List<String> reportImages;

    /**
     * 举报对象,1、资源博客,2、评论
     */
    @NotNull(message = "举报对象为必填项")
    @ParamRangeValid(ranges = {"1","2"}, message = "举报对象仅限1、2")
    private Integer reportTarget;

    @NotNull(message = "内容Id为必填项")
    private Integer reportTargetId;
}
