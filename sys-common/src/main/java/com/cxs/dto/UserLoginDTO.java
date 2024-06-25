package com.cxs.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class UserLoginDTO {

    @NotBlank(message = "用户名岂能为空")
    private String username;
    @NotBlank(message = "用户密码岂能为空")
    private String password;
}
