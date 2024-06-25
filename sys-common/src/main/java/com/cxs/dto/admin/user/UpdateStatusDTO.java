package com.cxs.dto.admin.user;

import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

  
@Data
public class UpdateStatusDTO {
    @NotBlank(message = "用户id为必传项")
    private String userId;

    @NotNull(message = "status为必传项")
    @ParamRangeValid(ranges = {"1", "2"}, message = "用户状态参数异常,1、正常 2、锁定")
    private Integer status;
}
