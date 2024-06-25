package com.cxs.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
public class CodeGenerateDTO {
    private String basePath;
    @NotBlank(message = "author为必传项")
    private String author;
    @NotBlank(message = "parentPackageName为必传项")
    private String parentPackageName;
    @NotBlank(message = "mapperPackageName为必传项")
    private String mapperPackageName;
    @NotBlank(message = "mapperXmlPackageName为必传项")
    private String mapperXmlPackageName;
    @NotBlank(message = "pojoPackageName为必传项")
    private String pojoPackageName;
    @NotBlank(message = "servicePackageName为必传项")
    private String servicePackageName;
    @NotBlank(message = "serviceImplPackageName为必传项")
    private String serviceImplPackageName;
    @NotBlank(message = "controllerPackageName为必传项")
    private String controllerPackageName;

    private List<String> tableNameList;

    private String ignoreTablePrefix;
}
