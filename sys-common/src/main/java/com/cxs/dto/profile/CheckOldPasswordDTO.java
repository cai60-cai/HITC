package com.cxs.dto.profile;

import lombok.Data;

import javax.validation.constraints.NotBlank;

  
@Data
public class CheckOldPasswordDTO {

    @NotBlank(message = "旧密码为必传项")
    private String oldPassword;
}
