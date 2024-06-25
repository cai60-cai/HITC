package com.cxs.dto.external;

import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

  
@Data
public class SaveOrUpdateExternalFriendLinkDTO {

    private Integer externalLinkId;

    /**
     * 链接名称
     */
    @NotBlank(message = "externalLinkName为必传项")
    private String externalLinkName;

    /**
     * 链接地址
     */
    @NotBlank(message = "externalLinkLink为必传项")
    private String externalLinkLink;

    /**
     * 链接图标
     */
    private String externalLinkIcon;

    /**
     * 链接打开方式
     */
    private String externalLinkBlank;

    /**
     * 状态,0、待审核1、已通过
     */
    @NotNull(message = "externalLinkStatus为必传项")
    @ParamRangeValid(ranges = {"0","1","2"}, message = "externalLinkStatus范围{0,1,2}")
    private Integer externalLinkStatus;

    /**
     * 备注
     */
    private String externalLinkDesc;
}
