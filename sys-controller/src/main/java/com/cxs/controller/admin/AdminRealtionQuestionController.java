package com.cxs.controller.admin;

import com.cxs.aspect.annotation.MarkLog;
import com.cxs.base.BaseResult;
import com.cxs.dto.admin.notice.GetNoticeListDTO;
import com.cxs.dto.admin.question.AdminGetQuestionListDTO;
import com.cxs.dto.admin.question.SaveOrUpdateQuestionDTO;
import com.cxs.service.RealtionQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/admin/question")
@Api(tags = "管理员系统相关问题控制器")
public class AdminRealtionQuestionController {

    @Autowired
    private RealtionQuestionService realtionQuestionService;


    @PostMapping("/saveOrUpdateQuestion")
    @ApiOperation("管理员新增或修改问题处理器")
    @MarkLog(desc = "管理员新增或修改问题")
    public BaseResult saveOrUpdateQuestion(@RequestBody SaveOrUpdateQuestionDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        Integer id = dto.getId();
        if (ObjectUtils.isEmpty(id)) {
            realtionQuestionService.saveQuestion(dto, request, result);
        } else {
            realtionQuestionService.updateQuestion(dto, request, result);
        }
        return result;
    }

    @DeleteMapping("/removeQuestion/{id}")
    @ApiOperation("管理员删除问题处理器")
    @MarkLog(desc = "管理员删除问题")
    public BaseResult removeQuestion(@PathVariable("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        realtionQuestionService.removeQuestion(id, request, result);
        return result;
    }

    @PostMapping("/getQuestionList")
    @ApiOperation("管理员查询问题处理器")
    public BaseResult getQuestionList(AdminGetQuestionListDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        realtionQuestionService.getQuestionList(dto, request, result);
        return result;
    }

    @GetMapping("/getQuestionInfo/{id}")
    @ApiOperation("管理员查询问题详情处理器")
    public BaseResult getQuestionInfo(@PathVariable("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        realtionQuestionService.getQuestionInfo(id, request, result);
        return result;
    }
}
