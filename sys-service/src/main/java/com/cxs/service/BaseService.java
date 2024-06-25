package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.dto.SendEmailCodeDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BaseService {
    /**
     * 文件上传
     * @param file
     * @return
     */
    void imgUploadFile(MultipartFile file, BaseResult result);

    /**
     * 服务器文件上传
     * @param file
     * @param result
     */
    void fileUpload(MultipartFile file, HttpServletRequest request, BaseResult result);

    void fileDownload(String fileId, HttpServletRequest request, HttpServletResponse response);

    /**
     * 发送邮箱验证码
     * @param dto
     * @param request
     * @param result
     */
    void sendEmailCode(SendEmailCodeDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取验证码code
     * @param result
     */
    void getValidateCode(BaseResult result);
}
