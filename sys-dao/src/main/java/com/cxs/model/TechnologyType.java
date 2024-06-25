package com.cxs.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 技术分类表
 * @TableName t_technology_type
 */
@TableName(value ="t_technology_type")
@Data
public class TechnologyType implements Serializable {
    /**
     * 分类id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名
     */
    private String typeName;

    /**
     * 父id, -1表示一级
     */
    private Integer typeParentId;

    /**
     * 分类链接
     */
    private String typeRoute;

    /**
     * 图标
     */
    private String typeIcon;

    /**
     * 0、未删除，1、已删除
     */
    private Integer typeDel;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}