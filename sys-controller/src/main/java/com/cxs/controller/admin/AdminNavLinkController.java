package com.cxs.controller.admin;

import com.cxs.aspect.annotation.MarkLog;
import com.cxs.base.BaseResult;
import com.cxs.dto.admin.nav.CreateOrUpdateNavLinkDTO;
import com.cxs.dto.admin.nav.NavLinkOrderMoveOrToppingOrDelDTO;
import com.cxs.service.NavLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/admin/nav")
@Api(tags = "管理员导航链接控制器")
public class AdminNavLinkController {

    @Autowired
    private NavLinkService navLinkService;


    @PostMapping("/createOrUpdateNavLink")
    @ApiOperation("新增、修改导航链接处理器")
    @MarkLog(desc = "新增、修改导航链接")
    public BaseResult createOrUpdateNavLink(@RequestBody @Validated CreateOrUpdateNavLinkDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        Integer navId = dto.getNavId();
        if (ObjectUtils.isEmpty(navId)) {
            navLinkService.createNavLink(dto, request, result);
        } else {
            navLinkService.updateNavLink(dto, request, result);
        }
        return result;
    }

    @PostMapping("/navLinkOrderMoveOrToppingOrDel")
    @ApiOperation("导航链接顺序移动、删除处理器")
    @MarkLog(desc = "导航链接顺序移动、删除")
    public BaseResult navLinkOrderMoveOrToppingOrDel(@RequestBody @Validated NavLinkOrderMoveOrToppingOrDelDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        navLinkService.navLinkOrderMoveOrToppingOrDel(dto, request, result);
        return result;
    }

    @GetMapping("/getLinkList")
    @ApiOperation("获取导航链接列表处理器")
    public BaseResult getLinkList(){
        BaseResult result = BaseResult.ok();
        navLinkService.getRealtimeLinkList(result);
        return result;
    }
}
