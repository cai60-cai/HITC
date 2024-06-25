package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.model.FilePath;
import com.baomidou.mybatisplus.extension.service.IService;


public interface FilePathService extends IService<FilePath> {
    void removeFileByFileId(String fileId, BaseResult result);
}
