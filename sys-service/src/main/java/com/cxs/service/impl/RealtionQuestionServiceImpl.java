package com.cxs.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BasePageBean;
import com.cxs.base.BaseResult;
import com.cxs.constant.CachePrefixContent;
import com.cxs.dto.admin.question.AdminGetQuestionListDTO;
import com.cxs.dto.admin.question.AdminQuestionListVO;
import com.cxs.dto.admin.question.SaveOrUpdateQuestionDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.model.Feedback;
import com.cxs.model.RealtionQuestion;
import com.cxs.model.User;
import com.cxs.model.UserAuth;
import com.cxs.model.UserRole;
import com.cxs.model.UserSetting;
import com.cxs.service.RealtionQuestionService;
import com.cxs.mapper.RealtionQuestionMapper;
import com.cxs.vo.admin.feedback.GetFeedBackListVO;
import com.cxs.vo.question.QuestionInfoVO;
import com.cxs.vo.question.QuestionListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author DELL
 * @description 针对表【t_realtion_question(问题回答表)】的数据库操作Service实现
 * @createDate 2024-05-23 19:03:10
 */
@Slf4j
@Service
public class RealtionQuestionServiceImpl extends ServiceImpl<RealtionQuestionMapper, RealtionQuestion> implements RealtionQuestionService {

    @Autowired
    private RealtionQuestionMapper realtionQuestionMapper;

    @Override
    public void questionList(BaseResult result, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            LambdaQueryWrapper<RealtionQuestion> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(RealtionQuestion::getId, RealtionQuestion::getQuestion).eq(RealtionQuestion::getIsShow, 1).orderByDesc(RealtionQuestion::getCreateTime);
            List<RealtionQuestion> realtionQuestions = realtionQuestionMapper.selectList(queryWrapper);
            List<QuestionListVO> voList = CollectionUtils.isEmpty(realtionQuestions) ? new ArrayList<>(0) : realtionQuestions.stream().map(r -> {
                QuestionListVO vo = new QuestionListVO();
                BeanUtils.copyProperties(r, vo);
                return vo;
            }).collect(Collectors.toList());
            result.setData(voList);
        } catch (Exception e) {
            log.error("获取问题列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取问题列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void questionInfo(Integer id, BaseResult result, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            RealtionQuestion realtionQuestion = realtionQuestionMapper.selectById(id);
            if (ObjectUtils.isEmpty(realtionQuestion)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该问题不存在");
            } else {
                if (realtionQuestion.getIsShow()) {
                    QuestionInfoVO vo = new QuestionInfoVO();
                    BeanUtils.copyProperties(realtionQuestion, vo);
                    result.setData(vo);
                } else {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该问题不存在");
                }
            }
        } catch (Exception e) {
            log.error("获取问题详情失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取问题详情接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, id, result);
        }
    }

    @Override
    public void saveQuestion(SaveOrUpdateQuestionDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            RealtionQuestion question = new RealtionQuestion();
            BeanUtils.copyProperties(dto, question);
            question.setCreateTime(LocalDateTime.now());
            int insert = realtionQuestionMapper.insert(question);
            if (insert != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
            }
            result.setData(question);
        } catch (Exception e) {
            log.error("新增问题失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【新增问题接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, dto, result);
        }
    }

    @Override
    public void updateQuestion(SaveOrUpdateQuestionDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            RealtionQuestion question = new RealtionQuestion();
            BeanUtils.copyProperties(dto, question);
            int update = realtionQuestionMapper.updateById(question);
            if (update != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
            }
        } catch (Exception e) {
            log.error("修改问题失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【修改问题接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void removeQuestion(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            realtionQuestionMapper.deleteById(id);
        } catch (Exception e) {
            log.error("删除问题失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【删除问题接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, id, result);
        }
    }

    @Override
    public void getQuestionList(AdminGetQuestionListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            BasePageBean pageBean = new BasePageBean();
            Page<RealtionQuestion> page = new Page(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<RealtionQuestion> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasLength(dto.getKeyword())) {
                queryWrapper.like(RealtionQuestion::getQuestion, dto.getKeyword());
            }
            IPage<RealtionQuestion> iPage = realtionQuestionMapper.selectPage(page, queryWrapper);
            List<RealtionQuestion> records = iPage.getRecords();
            List<AdminQuestionListVO> voList = CollectionUtils.isEmpty(records) ? new ArrayList<>(0) : records.stream().map(r -> {
                AdminQuestionListVO vo = new AdminQuestionListVO();
                BeanUtils.copyProperties(r, vo);
                return vo;
            }).collect(Collectors.toList());
            pageBean.setPageNum(iPage.getCurrent());
            pageBean.setPageSize(iPage.getSize());
            pageBean.setPages(iPage.getPages());
            pageBean.setTotal(iPage.getTotal());
            pageBean.setData(voList);
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("管理员查询问题失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员查询问题接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void getQuestionInfo(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            RealtionQuestion realtionQuestion = realtionQuestionMapper.selectById(id);
            if (ObjectUtils.isEmpty(realtionQuestion)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该问题不存在");
            } else {
                QuestionInfoVO vo = new QuestionInfoVO();
                BeanUtils.copyProperties(realtionQuestion, vo);
                result.setData(vo);
            }
        } catch (Exception e) {
            log.error("获取问题详情失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取问题详情接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, id, result);
        }
    }
}




