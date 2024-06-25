package com.cxs.vo.main;

import lombok.Data;



@Data
public class ExternalLinkVO {
    /**
     * 链接名称
     */
    private String externalLinkName;

    /**
     * 链接地址
     */
    private String externalLinkLink;

    /**
     * 链接图标
     */
    private String externalLinkIcon;

    /**
     * 链接打开方式
     */
    private String externalLinkBlank;


}
