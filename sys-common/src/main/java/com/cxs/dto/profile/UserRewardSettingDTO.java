package com.cxs.dto.profile;

import lombok.Data;

import javax.validation.constraints.NotNull;

  
@Data
public class UserRewardSettingDTO {

    @NotNull(message = "openReward为必传项")
    private Boolean openReward;
}
