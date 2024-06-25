package com.cxs.dto.admin.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

  
@Data
public class UpdateUserPwdDTO {
    @NotBlank(message = "旧密码为必传项")
    private String oldPwd;
    @NotBlank(message = "新密码为必传项")
    private String newPwd;
}
