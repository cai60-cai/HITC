package com.cxs.controller.admin;

import com.cxs.aspect.annotation.MarkLog;
import com.cxs.base.BaseResult;
import com.cxs.dto.admin.tag.SaveOrUpdateTagDTO;
import com.cxs.service.TagService;
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
@RequestMapping("/admin/tag")
@Api(tags = "管理员标签控制器")
public class AdminTagController {

    @Autowired
    private TagService tagService;

    @PostMapping("/saveOrUpdateTag")
    @ApiOperation("新增修改标签处理器")
    @MarkLog(desc = "新增修改标签")
    public BaseResult saveOrUpdateTag(@RequestBody @Validated SaveOrUpdateTagDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        Integer tagId = dto.getTagId();
        if (ObjectUtils.isEmpty(tagId)) {
            tagService.saveTag(dto, request, result);
        } else {
            tagService.updateTag(dto, request, result);
        }
        return result;
    }

    @DeleteMapping("/deleteTag/{id}")
    @ApiOperation("删除新增修改标签处理器")
    @MarkLog(desc = "删除新增修改标签")
    public BaseResult deleteTag(@PathVariable("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        tagService.deleteTag(id, request, result);
        return result;
    }

    @GetMapping("/getTagList")
    @ApiOperation("获取标签列表处理器")
    public BaseResult getTagList(){
        BaseResult result = BaseResult.ok();
        tagService.getRealTimeTagList(result);
        return result;
    }
}
