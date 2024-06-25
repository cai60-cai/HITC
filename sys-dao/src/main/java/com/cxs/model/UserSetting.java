package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户设置表
 * @TableName t_user_setting
 */
@TableName(value ="t_user_setting")
@Data
public class UserSetting implements Serializable {
    /**
     * 用户id
     */
    @TableId
    private String userId;

    /**
     * 打赏功能,0、未开启 1、已开启
     */
    private Boolean openReward;

    /**
     * 显示积分余额,0、不显示 1、显示
     */
    private Boolean showPoint;

    /**
     * 接收邮件通知
     */
    private Boolean receiveEmailNotice;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}