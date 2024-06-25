package com.cxs.dto.profile;

import lombok.Data;

import javax.validation.constraints.NotBlank;

  
@Data
public class UpdatePwdDTO {
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    @NotBlank(message = "新密码不能为空")
    private String password;
}
