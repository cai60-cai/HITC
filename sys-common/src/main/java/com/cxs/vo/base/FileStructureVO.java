package com.cxs.vo.base;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class FileStructureVO {

    private String name;

    private Long size;

    private Boolean disabled = true;

    private List<FileStructureVO> children = new ArrayList<>();
}
