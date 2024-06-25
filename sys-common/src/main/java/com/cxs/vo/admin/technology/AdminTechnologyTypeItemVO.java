package com.cxs.vo.admin.technology;

import lombok.Data;


@Data
public class AdminTechnologyTypeItemVO {
    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String typeName;

    /**
     * 父id
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


    private Integer typeDel;
}
