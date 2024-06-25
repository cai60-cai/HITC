package com.cxs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BasePageBean;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.dto.admin.feedback.GetFeedBackListDTO;
import com.cxs.dto.admin.feedback.HandleFeedBackDTO;
import com.cxs.dto.feedback.AddFeedbackDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.mapper.FeedbackReplyMapper;
import com.cxs.mapper.UserAuthMapper;
import com.cxs.model.ExternalLink;
import com.cxs.model.Feedback;
import com.cxs.model.FeedbackReply;
import com.cxs.model.Report;
import com.cxs.model.User;
import com.cxs.model.UserAuth;
import com.cxs.service.FeedbackService;
import com.cxs.mapper.FeedbackMapper;
import com.cxs.service.UserService;
import com.cxs.utils.SendMailUtil;
import com.cxs.utils.StringUtil;
import com.cxs.vo.admin.feedback.AdminGetFeedBackInfoVO;
import com.cxs.vo.admin.feedback.GetFeedBackListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author lenovo
* @description 针对表【t_feedback(用户反馈表)】的数据库操作Service实现
* @createDate 2024-05-18 10:43:37
*/
@Slf4j
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService{

    @Autowired
    private UserService userService;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private FeedbackReplyMapper feedbackReplyMapper;

    @Autowired
    private SendMailUtil sendMailUtil;

    @Override
    public void addFeedback(AddFeedbackDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            // 获取用户权限
            UserAuth userAuth = userAuthMapper.selectById(userByToken.getId());
            if (!userAuth.getFeedbackAuth()) {
                result.setCode(CurrencyErrorEnum.AUTH_LOCK.getCode());
                result.setMsg(CurrencyErrorEnum.AUTH_LOCK.getMsg() + ",暂无法进行反馈,请联系管理员");
            } else {
                Feedback feedback = new Feedback();
                BeanUtils.copyProperties(dto, feedback);
                feedback.setFeedbackUser(userByToken.getId());
                // 处理图片
                if (!CollectionUtils.isEmpty(dto.getFeedbackImages())) {
                    String images = StringUtil.joinListToString(dto.getFeedbackImages(), ",");
                    feedback.setFeedbackImages(images);
                }
                feedback.setFeedbackTime(LocalDateTime.now());
                int insert = feedbackMapper.insert(feedback);
                if (insert != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",新增反馈失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户添加反馈失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户添加反馈接口】【{}ms】 \n入参:{}\n出参:{}", "添加", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void getFeedBackList(GetFeedBackListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            BasePageBean pageBean = new BasePageBean();
            Page<Feedback> page = new Page(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<Feedback> queryWrapper = new LambdaQueryWrapper<>();
            if (!ObjectUtils.isEmpty(dto.getFeedbackStatus())) {
                queryWrapper.eq(Feedback::getFeedbackStatus, dto.getFeedbackStatus());
            }
            IPage<Feedback> iPage = feedbackMapper.selectPage(page, queryWrapper);
            List<Feedback> records = iPage.getRecords();
            List<GetFeedBackListVO> voList = CollectionUtils.isEmpty(records) ? new ArrayList<>(0) :
                    records.stream().map(r -> {
                        GetFeedBackListVO vo = new GetFeedBackListVO();
                        BeanUtils.copyProperties(r, vo);
                        String images = r.getFeedbackImages();
                        if (StringUtils.hasLength(images)) {
                            vo.setFeedbackImages(Arrays.asList(images.split(",")));
                        }
                        return vo;
                    }).collect(Collectors.toList());
            pageBean.setPageNum(iPage.getCurrent());
            pageBean.setPageSize(iPage.getSize());
            pageBean.setPages(iPage.getPages());
            pageBean.setTotal(iPage.getTotal());
            pageBean.setData(voList);
            result.setData(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员获取用户反馈列表失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取用户反馈列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    @Transactional
    public void handleFeedBack(HandleFeedBackDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            Integer feedbackId = dto.getFeedbackId();
            Feedback feedback = feedbackMapper.selectById(feedbackId);
            if (ObjectUtils.isEmpty(feedback)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",反馈不存在");
            } else {
                Integer emailFlag = dto.getEmailFlag();
                FeedbackReply reply = new FeedbackReply();
                BeanUtils.copyProperties(dto, reply);
                reply.setAdminId(userByToken.getId());
                reply.setReplyTime(LocalDateTime.now());
                int insert = feedbackReplyMapper.insert(reply);
                if (insert == 1) {
                    Feedback f = new Feedback();
                    f.setFeedbackId(feedback.getFeedbackId());
                    f.setFeedbackStatus(1);
                    int update = feedbackMapper.updateById(f);
                    if (update != 1) {
                        result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",处理反馈失败");
                    } else {
                        if (emailFlag == 1) {
                            // 发邮件
                            String email = "";
                            if (StringUtils.hasLength(feedback.getFeedbackEmail())) {
                                email = feedback.getFeedbackEmail();
                            } else {
                                User byId = userService.getById(feedback.getFeedbackUser());
                                if (!ObjectUtils.isEmpty(byId) && StringUtils.hasLength(byId.getEmail())) {
                                    email = byId.getEmail();
                                }
                            }
                            if (StringUtils.hasLength(email)) {
                                sendMailUtil.sendMail(email, "反馈处理通知", sendMailUtil.buildFeedbackContent(feedback, reply));
                            }
                        }
                    }
                } else {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",处理反馈失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员处理反馈列表失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员处理反馈列表接口】【{}ms】 \n入参:{}\n出参:{}", "新增&修改", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void delFeedback(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            Feedback feedback = feedbackMapper.selectById(id);
            if (ObjectUtils.isEmpty(feedback) || !feedback.getFeedbackUser().equals(userByToken.getId())) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",反馈信息不存在");
            } else {
                int res = feedbackMapper.deleteById(id);
                if (res <= 0) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",删除失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户删除反馈失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户删除反馈接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, id, result);
        }
    }

    @Override
    public void adminGetFeedBackInfo(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Feedback feedback = feedbackMapper.selectById(id);
            if (ObjectUtils.isEmpty(feedback)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",反馈信息不存在");
            } else {
                AdminGetFeedBackInfoVO vo = new AdminGetFeedBackInfoVO();
                BeanUtils.copyProperties(feedback, vo);
                String images = feedback.getFeedbackImages();
                if (StringUtils.hasLength(images)) {
                    vo.setFeedbackImages(Arrays.asList(images.split(",")));
                }
                LambdaQueryWrapper<FeedbackReply> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(FeedbackReply::getFeedbackId, feedback.getFeedbackId());
                FeedbackReply feedbackReply = feedbackReplyMapper.selectOne(wrapper);
                if (null != feedbackReply) {
                    vo.setReplyContent(feedbackReply.getReplyContent());
                    vo.setReplyTime(feedbackReply.getReplyTime());
                }
                result.setData(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员获取用户反馈详情失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取用户反馈详情】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, id, result);
        }
    }

    @Override
    public void adminDelFeedback(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Feedback feedback = feedbackMapper.selectById(id);
            if (ObjectUtils.isEmpty(feedback) ) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",反馈信息不存在");
            } else {
                int res = feedbackMapper.deleteById(id);
                if (res <= 0) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",删除失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员删除反馈失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员删除反馈接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, id, result);
        }
    }
}




