package com.cxs.dto.profile;

import lombok.Data;

import javax.validation.constraints.NotNull;

  
@Data
public class UserPointSettingDTO {

    @NotNull(message = "showPoint为必传项")
    private Boolean showPoint;
}
