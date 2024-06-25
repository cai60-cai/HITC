package com.cxs.dto.admin.tag;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class SaveOrUpdateTagDTO {
    private Integer tagId;

    @NotBlank(message = "tagName为必传项")
    private String tagName;
}
