package com.cxs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.model.Article;
import com.cxs.model.ArticleTag;
import com.cxs.model.FilePath;
import com.cxs.service.FilePathService;
import com.cxs.mapper.FilePathMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class FilePathServiceImpl extends ServiceImpl<FilePathMapper, FilePath> implements FilePathService{

    @Autowired
    private FilePathMapper filePathMapper;

    @Override
    public void removeFileByFileId(String fileId, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            FilePath filePath = filePathMapper.selectById(fileId);
            if (ObjectUtils.isEmpty(filePath)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文件不存在");
            } else {
                int delete = filePathMapper.deleteById(fileId);
                if (delete == 1) {
                    // 删除文件
                    FileSystemUtils.deleteRecursively(new File(filePath.getFileRealPath()));
                } else {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",文件删除失败");
                }
            }
        } catch (Exception e) {
            log.error("文件删除失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【文件删除接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, fileId, result);
        }
    }
}




