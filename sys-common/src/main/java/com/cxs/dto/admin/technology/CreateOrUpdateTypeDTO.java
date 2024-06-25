package com.cxs.dto.admin.technology;

import com.cxs.valid.annotation.ParamRangeValid;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Data
public class CreateOrUpdateTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 分类名
     */
    @NotBlank(message = "typeName为必传项")
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

    @ParamRangeValid(ranges = {"0", "1"}, message = "typeDel范围{0, 1}")
    private Integer typeDel;
}
