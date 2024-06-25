package com.cxs.dto.profile;

import com.cxs.valid.annotation.ParamLengthValid;
import lombok.Data;

import javax.validation.constraints.NotBlank;

  
@Data
public class CheckEmailBindDTO {

    @NotBlank(message = "邮箱为必传项")
    private String email;
}
