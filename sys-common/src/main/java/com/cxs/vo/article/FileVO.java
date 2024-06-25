package com.cxs.vo.article;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class FileVO {
    private String fileId;
    private String fileName;
    private Long fileSize;
    private JSONArray fileStructure;
}
