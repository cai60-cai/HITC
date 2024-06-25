package com.cxs.vo.file;

import lombok.Data;


@Data
public class FileDownloadVO {

    private String fileId;

    private String file;

    private Long fileSize;

    private String fileName;

    private String filePostfix;
}
