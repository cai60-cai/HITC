package com.cxs.service.admin;

import com.cxs.base.BaseResult;
import com.cxs.dto.CodeGenerateDTO;
import com.cxs.dto.admin.GetTableListDTO;

import javax.servlet.http.HttpServletResponse;


public interface AdminCodeService {
    /**
     * 获取数据库表信息
     * @param dto
     * @param result
     */
    void getTableList(GetTableListDTO dto, BaseResult result);

    /**
     * 代码生成
     * @param dto
     * @param response
     */
    void codeGenerate(CodeGenerateDTO dto, HttpServletResponse response);
}
