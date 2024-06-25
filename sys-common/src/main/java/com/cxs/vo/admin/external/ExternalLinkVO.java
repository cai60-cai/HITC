package com.cxs.vo.admin.external;

import com.alibaba.fastjson.annotation.JSONField;
import com.cxs.vo.user.UserVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExternalLinkVO {
    private Integer externalLinkId;

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

    /**
     * 状态,0、待审核1、已通过
     */
    private Integer externalLinkStatus;

    /**
     * 所属用户
     */
    private UserVO user;

    /**
     * 备注
     */
    private String externalLinkDesc;

    /**
     * 链接创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 链接修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
