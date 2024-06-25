package com.cxs.dto.admin.menu;

import lombok.Data;

import java.util.List;


@Data
public class DeleteMenusDTO {
    private List<Integer> menuIds;
}
