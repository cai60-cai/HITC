package com.cxs.vo.article;

import lombok.Data;


@Data
public class DownloadBeforeValidVO {
    private Boolean flag = false;
    private String downToken;
}
