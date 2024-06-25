package com.cxs.dto.admin.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

  
@Data
public class UpdateUserInfoDTO {

    @NotBlank(message = "userName为必传项")
    private String userName;

    private String avatar;

    private String email;

    private String phone;

    private String autograph;
}
