package com.cxs.controller.admin;

import com.cxs.aspect.annotation.MarkLog;
import com.cxs.base.BaseResult;
import com.cxs.dto.admin.technology.CreateOrUpdateTypeDTO;
import com.cxs.dto.admin.technology.GetFirstTypeListDTO;
import com.cxs.dto.admin.technology.GetSecondTypeListDTO;
import com.cxs.service.TechnologyTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/admin/technology")
@Api(tags = "管理员技术分类控制器")
public class AdminTechnologyTypeController {

    @Autowired
    private TechnologyTypeService technologyTypeService;

    @PostMapping("/getFirstTypeList")
    @ApiOperation("获取一级技术分类处理器")
    public BaseResult getFirstTypeList(@RequestBody GetFirstTypeListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        technologyTypeService.getFirstTypeList(dto, request, result);
        return result;
    }

    @PostMapping("/getSecondTypeList")
    @ApiOperation("获取二级技术分类处理器")
    public BaseResult getSecondTypeList(@RequestBody @Validated GetSecondTypeListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        technologyTypeService.getSecondTypeList(dto, request, result);
        return result;
    }

    @PostMapping("/createOrUpdateType")
    @ApiOperation("新增、修改技术分类处理器")
    @MarkLog(desc = "新增、修改技术分类")
    public BaseResult createOrUpdateType(@RequestBody @Validated CreateOrUpdateTypeDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        Integer id = dto.getId();
        if (ObjectUtils.isEmpty(id)) {
            technologyTypeService.createType(dto, request, result);
        } else {
            technologyTypeService.updateType(dto, request, result);
        }
        return result;
    }

    @PostMapping("/getAdminTechnologyTypeList")
    @ApiOperation("获取树形技术分类处理器")
    public BaseResult getAdminTechnologyTypeList(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        technologyTypeService.getAdminTechnologyTypeList(request, result);
        return result;
    }

    @DeleteMapping("/deleteAdminTechnologyType/{id}")
    @ApiOperation("删除技术分类处理器")
    @MarkLog(desc = "删除技术分类")
    public BaseResult deleteAdminTechnologyType(@PathVariable("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        technologyTypeService.deleteAdminTechnologyType(id, request, result);
        return result;
    }
}
