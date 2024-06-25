package com.cxs.controller;

import com.cxs.base.BaseResult;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.exception.CurrencyException;
import com.cxs.service.BaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/base")
@Api(tags = "公共控制器")
public class BaseController {

    @Autowired
    private BaseService baseService;

    /**
     * 图片上传接口
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/img/upload")
    @ApiOperation("图片上传处理器")
    public BaseResult<String> upload(@RequestParam("file") @ApiParam(value = "文件",required = true) MultipartFile file) {
        if (ObjectUtils.isEmpty(file)){
            throw new CurrencyException(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR);
        }
        BaseResult result = BaseResult.ok();
        baseService.imgUploadFile(file, result);
        return result;
    }

    /**
     * 文件上传接口
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/file/upload")
    @ApiOperation("文件上传处理器")
    public BaseResult<String> fileUpload(@RequestParam("file") @ApiParam(value = "文件",required = true) MultipartFile file, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(file)){
            throw new CurrencyException(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR);
        }
        BaseResult result = BaseResult.ok();
        baseService.fileUpload(file, request, result);
        return result;
    }

    /**
     * 文件上传接口
     * @param fileId
     * @return
     * @throws Exception
     */
    @PostMapping("/file/download")
    @ApiOperation("文件下载处理器")
    public void fileDownload(@RequestParam("fileId") String fileId, HttpServletRequest request, HttpServletResponse response) {
        baseService.fileDownload(fileId, request, response);
    }
}
