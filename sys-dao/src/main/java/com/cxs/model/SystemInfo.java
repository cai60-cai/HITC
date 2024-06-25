package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 站点系统表
 * @TableName t_system_info
 */
@TableName(value ="t_system_info")
@Data
public class SystemInfo implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 站点标题
     */
    private String sysTitle;

    /**
     * 站点说明
     */
    private String sysGraph;

    /**
     * 站点内容
     */
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
     * 站点logo
     */
    private String sysLogo;

    /**
     * header名称下说明
     */
    private String sysLogoTitle;

    /**
     * 站点联系邮箱
     */
    private String sysEmail;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}