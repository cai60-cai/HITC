package com.cxs.controller.admin;

import com.cxs.aspect.annotation.MarkLog;
import com.cxs.base.BaseResult;
import com.cxs.dto.admin.external.GetExternalLinkListDTO;
import com.cxs.dto.external.ApproveExternalFriendLinkDTO;
import com.cxs.dto.external.SaveOrUpdateExternalFriendLinkDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.ExternalLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/external-link")
@Api(tags = "管理员友情链接控制器")
public class AdminExternalLinkController {

    @Autowired
    private ExternalLinkService externalLinkService;

    @PostMapping("/getExternalFriendLinkList")
    @ApiOperation("获取友情链接列表处理器")
    public BaseResult getExternalFriendLinkList(@RequestBody @Validated GetExternalLinkListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (dto.getPageNum() <= 0) dto.setPageNum(1);
        if (dto.getPageSize() < 0) dto.setPageSize(10);
        externalLinkService.getExternalFriendLinkList(dto, request, result);
        return result;
    }

    @PostMapping("/approveExternalFriendLink")
    @ApiOperation("审核友情链接处理器")
    @MarkLog(desc = "审核友情链接")
    public BaseResult approveExternalFriendLink(@RequestBody @Validated ApproveExternalFriendLinkDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        externalLinkService.approveExternalFriendLink(dto, request, result);
        return result;
    }

    @PostMapping("/saveOrUpdateExternalFriendLink")
    @ApiOperation("新增/修改友情链接处理器")
    @MarkLog(desc = "新增/修改友情链接")
    public BaseResult saveOrUpdateExternalFriendLink(@RequestBody @Validated SaveOrUpdateExternalFriendLinkDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (StringUtils.hasLength(dto.getExternalLinkLink())) {
            if (!dto.getExternalLinkLink().startsWith("http://") && !dto.getExternalLinkLink().startsWith("https://")) {
                result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",链接地址有误");
                return result;
            }
        }
        Integer externalLinkId = dto.getExternalLinkId();
        if (ObjectUtils.isEmpty(externalLinkId)) {
            externalLinkService.saveExternalFriendLink(dto, request, result);
        } else {
            externalLinkService.updateExternalFriendLink(dto, request, result);
        }
        return result;
    }

    @DeleteMapping("/deleteExternalFriendLink/{id}")
    @ApiOperation("删除友情链接处理器")
    @MarkLog(desc = "删除友情链接")
    public BaseResult deleteExternalFriendLink(@PathVariable("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        externalLinkService.deleteExternalFriendLink(id, request, result);
        return result;
    }
}
