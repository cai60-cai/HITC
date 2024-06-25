package com.cxs.controller.admin;

import com.cxs.base.BaseResult;
import com.cxs.dto.admin.GetTableListDTO;
import com.cxs.service.admin.AdminCommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/common")
@Api(tags = "管理员通用控制器")
public class AdminCommonController {

    @Autowired
    private AdminCommonService adminCommonService;

    @GetMapping("/getEncryptStr")
    @ApiOperation("获取加密字符串处理器")
    public BaseResult getEncryptStr(@RequestParam("keyword") String keyword){
        BaseResult result = BaseResult.ok();
        adminCommonService.getEncryptStr(keyword, result);
        return result;
    }

    @GetMapping("/getDecryptStr")
    @ApiOperation("获取解密字符串处理器")
    public BaseResult getDecryptStr(@RequestParam("encryptStr") String encryptStr){
        BaseResult result = BaseResult.ok();
        adminCommonService.getDecryptStr(encryptStr, result);
        return result;
    }
}
