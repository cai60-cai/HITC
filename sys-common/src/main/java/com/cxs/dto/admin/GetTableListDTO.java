package com.cxs.dto.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetTableListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String keyword;
}
