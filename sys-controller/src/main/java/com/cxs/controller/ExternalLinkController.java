package com.cxs.controller;

import com.cxs.base.BaseResult;
import com.cxs.constant.CommonContent;
import com.cxs.dto.external.ApplyExternalLinkDTO;
import com.cxs.service.ExternalLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/external")
@Api(tags = "友情链接控制器")
public class ExternalLinkController {

    @Autowired
    private ExternalLinkService externalLinkService;

    @PostMapping("/applyExternalLink")
    @ApiOperation("用户申请外部友情链接处理器")
    public BaseResult applyExternalLink(@RequestBody @Validated ApplyExternalLinkDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        externalLinkService.applyExternalLink(dto, request, result);
        return result;
    }
}
