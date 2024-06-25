package com.cxs.vo.technology;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class TechnologyTypeVO implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /**
     * 子节点
     */
    private List<TechnologyTypeItemVO> children = new ArrayList<>();
}
