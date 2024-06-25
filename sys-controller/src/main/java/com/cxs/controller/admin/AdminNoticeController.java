package com.cxs.controller.admin;

import com.cxs.aspect.annotation.MarkLog;
import com.cxs.base.BaseResult;
import com.cxs.dto.admin.notice.GetNoticeListDTO;
import com.cxs.dto.admin.notice.SaveOrUpdateNoticeDTO;
import com.cxs.service.NoticeInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/admin/notice")
@Api(tags = "管理员公告管控制器")
public class AdminNoticeController {

    @Autowired
    private NoticeInfoService noticeInfoService;

    @PostMapping("/getNoticeList")
    @ApiOperation("管理员获取公告列表处理器")
    public BaseResult getNoticeList(@RequestBody GetNoticeListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        noticeInfoService.getNoticeList(dto, request, result);
        return result;
    }

    @GetMapping("/getNoticeInfo")
    @ApiOperation("管理员获取公告详情处理器")
    public BaseResult getNoticeInfo(@RequestParam("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        noticeInfoService.getNoticeInfo(id, request, result);
        return result;
    }

    @PostMapping("/saveOrUpdateNotice")
    @ApiOperation("管理员创建或修改公告处理器")
    @MarkLog(desc = "管理员创建或修改公告")
    public BaseResult saveOrUpdateNotice(@RequestBody @Validated SaveOrUpdateNoticeDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        noticeInfoService.saveOrUpdateNotice(dto, request, result);
        return result;
    }

    @PostMapping("/deleteNotice/{id}")
    @ApiOperation("管理员删除公告处理器")
    @MarkLog(desc = "管理员删除公告")
    public BaseResult deleteNotice(@PathVariable("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        noticeInfoService.deleteNotice(id, request, result);
        return result;
    }
}
