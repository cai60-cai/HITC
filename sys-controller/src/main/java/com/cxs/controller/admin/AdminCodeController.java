package com.cxs.controller.admin;

import com.cxs.base.BaseResult;
import com.cxs.dto.CodeGenerateDTO;
import com.cxs.dto.admin.GetTableListDTO;
import com.cxs.service.admin.AdminCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/admin/code")
@Api(tags = "管理员代码生成控制器")
public class AdminCodeController {

    @Autowired
    private AdminCodeService adminCodeService;


    @PostMapping("/getTableList")
    @ApiOperation("获取数据表列表处理器")
    public BaseResult getTableList(@RequestBody GetTableListDTO dto){
        BaseResult result = BaseResult.ok();
        adminCodeService.getTableList(dto, result);
        return result;
    }

    @PostMapping("/codeGenerate")
    @ApiOperation("代码生成处理器")
    public void codeGenerate(@RequestBody @Validated CodeGenerateDTO dto, HttpServletResponse response){
        adminCodeService.codeGenerate(dto, response);
    }


}
