package com.cxs.vo.technology;

import lombok.Data;


@Data
public class TechnologyTypeItemVO {
    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String typeName;

    /**
     * 分类链接
     */
    private String typeRoute;

    /**
     * 图标
     */
    private String typeIcon;
}
