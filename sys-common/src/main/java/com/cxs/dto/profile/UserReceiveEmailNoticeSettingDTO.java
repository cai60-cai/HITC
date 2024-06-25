package com.cxs.dto.profile;

import lombok.Data;

import javax.validation.constraints.NotNull;

  
@Data
public class UserReceiveEmailNoticeSettingDTO {

    @NotNull(message = "receiveEmailNotice为必传项")
    private Boolean receiveEmailNotice;
}
