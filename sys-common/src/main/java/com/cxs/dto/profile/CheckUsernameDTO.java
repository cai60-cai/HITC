package com.cxs.dto.profile;

import com.cxs.valid.annotation.ParamLengthValid;
import lombok.Data;

import javax.validation.constraints.NotBlank;

  
@Data
public class CheckUsernameDTO {

    @NotBlank(message = "用户名为必传项")
    private String userName;
}
