package com.cxs.dto.point;

import lombok.Data;

import javax.validation.constraints.NotNull;

  
@Data
public class PointTradeDTO {

    @NotNull(message = "类型id为必传项")
    private Integer id;
}
