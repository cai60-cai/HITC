package com.cxs.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class SendEmailCodeDTO {

    @NotBlank(message = "email岂能为空")
    private String email;
}
