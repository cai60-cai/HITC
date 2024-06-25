package com.cxs.vo.main;

import lombok.Data;

@Data
public class AboutSysInfoVO {

    /**
     * 站点标题
     */
    private String sysTitle;

    /**
     * 站点说明
     */
    private String sysGraph;

    private String sysContent;

    /**
     * 站点微信
     */
    private String sysWeixin;

    /**
     * 微信公众号
     */
    private String sysWenxinPublic;

    /**
     * 站点联系邮箱
     */
    private String sysEmail;

    /**
     * 站点logo
     */
    private String sysLogo;

    private String sysLogoTitle;

    private Integer articleCount;

    private Integer originArticleCount;

    private Integer userCount;

    private Integer tagCount;

    private Integer typeCount;
}
