package com.cxs.base;

import lombok.Data;

import java.io.Serializable;


@Data
public class BaseRequest implements Serializable {

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}
