package com.cxs.dto.article;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

  
@Data
public class DownloadFileDTO {

    @NotNull(message = "articleId为必传项")
    private Integer articleId;

    @NotBlank(message = "fileId为必传项")
    private String fileId;
}
