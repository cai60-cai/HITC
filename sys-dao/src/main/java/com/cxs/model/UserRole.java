package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户角色关联表
 * @TableName t_user_role
 */
@TableName(value ="t_user_role")
@Data
public class UserRole implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 角色id
     */
    private Integer roleId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}