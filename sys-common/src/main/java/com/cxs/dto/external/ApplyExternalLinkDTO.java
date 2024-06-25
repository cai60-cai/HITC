package com.cxs.dto.external;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class ApplyExternalLinkDTO implements Serializable {

    /**
     * 链接名称
     */
    @NotBlank(message = "链接名称岂能为空")
    private String externalLinkName;

    /**
     * 链接地址
     */
    @NotBlank(message = "链接地址岂能为空")
    private String externalLinkLink;

    /**
     * 链接图标
     */
    private String externalLinkIcon;
}