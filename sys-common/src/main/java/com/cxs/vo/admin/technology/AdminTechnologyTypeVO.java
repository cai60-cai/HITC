package com.cxs.vo.admin.technology;

import com.cxs.vo.technology.TechnologyTypeItemVO;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class AdminTechnologyTypeVO implements Serializable {

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

    /**
     * 子节点
     */
    private List<AdminTechnologyTypeItemVO> children = new ArrayList<>();
}
