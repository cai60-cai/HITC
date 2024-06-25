package com.cxs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BasePageBean;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.dto.admin.report.AdminGetReportListDTO;
import com.cxs.dto.admin.report.HandleReportDTO;
import com.cxs.dto.report.AddReportDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.enums.ReportHandleEnum;
import com.cxs.mapper.ArticleCommentMapper;
import com.cxs.mapper.ArticleMapper;
import com.cxs.mapper.FilePathMapper;
import com.cxs.mapper.UserAuthMapper;
import com.cxs.mapper.UserAuthScheduledMapper;
import com.cxs.model.Article;
import com.cxs.model.ArticleComment;
import com.cxs.model.Feedback;
import com.cxs.model.Report;
import com.cxs.model.FeedbackReply;
import com.cxs.model.FilePath;
import com.cxs.model.Report;
import com.cxs.model.User;
import com.cxs.model.UserAuth;
import com.cxs.model.UserAuthScheduled;
import com.cxs.service.ArticleCommentService;
import com.cxs.service.ArticleService;
import com.cxs.service.ReportService;
import com.cxs.mapper.ReportMapper;
import com.cxs.service.UserService;
import com.cxs.utils.SendMailUtil;
import com.cxs.utils.StringUtil;
import com.cxs.vo.admin.article.AdminArticleInfoVO;
import com.cxs.vo.admin.comment.AdminCommentVO;
import com.cxs.vo.admin.report.AdminGetFeedBackInfoVO;
import com.cxs.vo.admin.report.GetReportHandleTypeVO;
import com.cxs.vo.admin.report.GetReportListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author lenovo
* @description 针对表【t_report(金币充值类型)】的数据库操作Service实现
* @createDate 2024-05-12 14:08:44
*/
@Slf4j
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService{

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private SendMailUtil sendMailUtil;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private FilePathMapper filePathMapper;

    @Autowired
    private ArticleCommentMapper articleCommentMapper;

    @Autowired
    private UserAuthScheduledMapper userAuthScheduledMapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleCommentService articleCommentService;

    @Override
    public void addReport(AddReportDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            // 获取用户权限
            UserAuth userAuth = userAuthMapper.selectById(userByToken.getId());
            if (!userAuth.getReportAuth()) {
                result.setCode(CurrencyErrorEnum.AUTH_LOCK.getCode());
                result.setMsg(CurrencyErrorEnum.AUTH_LOCK.getMsg() + ",暂无法进行举报,请联系管理员");
            } else {
                Report report = new Report();
                BeanUtils.copyProperties(dto, report);
                report.setReportUserId(userByToken.getId());
                // 处理图片
                if (!CollectionUtils.isEmpty(dto.getReportImages())) {
                    String images = StringUtil.joinListToString(dto.getReportImages(), ",");
                    report.setReportImages(images);
                }
                report.setReportTime(LocalDateTime.now());
                int insert = reportMapper.insert(report);
                if (insert != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",举报失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户举报失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户举报接口】【{}ms】 \n入参:{}\n出参:{}", "添加", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void delReport(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            Report report = reportMapper.selectById(id);
            if (ObjectUtils.isEmpty(report) || !report.getReportUserId().equals(userByToken.getId())) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",举报信息不存在");
            } else {
                int res = reportMapper.deleteById(id);
                if (res <= 0) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",删除失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户删除举报失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户删除举报接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, id, result);
        }
    }

    @Override
    public void adminGetReportList(AdminGetReportListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            BasePageBean pageBean = new BasePageBean();
            Page<Report> page = new Page(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<Report> queryWrapper = new LambdaQueryWrapper<>();
            if (!ObjectUtils.isEmpty(dto.getReportStatus())) {
                queryWrapper.eq(Report::getReportStatus, dto.getReportStatus());
            }
            queryWrapper.orderByAsc(Report::getReportTime);
            IPage<Report> iPage = reportMapper.selectPage(page, queryWrapper);
            List<Report> records = iPage.getRecords();
            List<GetReportListVO> voList = CollectionUtils.isEmpty(records) ? new ArrayList<>(0) :
                    records.stream().map(r -> {
                        GetReportListVO vo = new GetReportListVO();
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
            e.printStackTrace();
            log.error("管理员获取用户举报列表失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取用户举报列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    @Transactional
    public void adminHandleReport(HandleReportDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            Integer reportId = dto.getReportId();
            Report report = reportMapper.selectById(reportId);
            if (ObjectUtils.isEmpty(report)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",举报不存在");
            } else {
                Integer reportTarget = report.getReportTarget();
                String handleNo = dto.getHandleNo();
                // 博客
                if (reportTarget == 1) {
                    Report handle = new Report();
                    if (ReportHandleEnum.ARTICLE_NO_RESULT.getHandleNo().equals(handleNo)){
                        BeanUtils.copyProperties(dto, handle);
                        // 举报不实
                        handle.setReportId(reportId);
                        handle.setReportStatus(1);
                        handle.setReportHandleResult(ReportHandleEnum.ARTICLE_NO_RESULT.getHandleDesc());
                        handle.setReportHandleTime(LocalDateTime.now());
                        handle.setReportHandleAdmin(userByToken.getId());
                        int update = reportMapper.updateById(handle);
                        if (update <= 0) {
                            result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",处理失败");
                        }
                    } else if (ReportHandleEnum.ARTICLE_DELETE.getHandleNo().equals(handleNo)) {
                        // 删除源文件
                        BeanUtils.copyProperties(dto, handle);
                        handle.setReportId(reportId);
                        handle.setReportStatus(1);
                        handle.setReportHandleResult(ReportHandleEnum.ARTICLE_DELETE.getHandleDesc());
                        handle.setReportHandleTime(LocalDateTime.now());
                        handle.setReportHandleAdmin(userByToken.getId());
                        int update = reportMapper.updateById(handle);
                        if (update <= 0) {
                            result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",处理失败");
                        } else {
                            // 删除博客
                            Integer articleId = report.getReportTargetId();
                            Article article = articleMapper.selectById(articleId);
                            if (!ObjectUtils.isEmpty(article)) {
                                deleteArticleInfo(articleId, article.getArticleFileId());
                            }
                        }
                    } else if (ReportHandleEnum.ARTICLE_DELETE_AND_BAN_PUBLISH.getHandleNo().equals(handleNo)) {
                        // 删除源文件,禁止发布文章
                        BeanUtils.copyProperties(dto, handle);
                        handle.setReportId(reportId);
                        handle.setReportStatus(1);
                        handle.setReportHandleResult(ReportHandleEnum.ARTICLE_DELETE_AND_BAN_PUBLISH.getHandleDesc() + dto.getBanMinute() + "分钟");
                        handle.setReportHandleTime(LocalDateTime.now());
                        handle.setReportHandleAdmin(userByToken.getId());
                        int update = reportMapper.updateById(handle);
                        if (update == 1) {
                            // 删除博客
                            Integer articleId = report.getReportTargetId();
                            Article article = articleMapper.selectById(articleId);
                            if (!ObjectUtils.isEmpty(article)) {
                                deleteArticleInfo(articleId, article.getArticleFileId());
                                // 禁止用户发布权限
                                UserAuth userAuth = new UserAuth();
                                userAuth.setUserId(article.getArticleBelongUserId());
                                userAuth.setPushArticleAuth(Boolean.FALSE);
                                userAuthMapper.updateById(userAuth);

                                UserAuthScheduled userAuthScheduled = new UserAuthScheduled();
                                userAuthScheduled.setUserId(article.getArticleBelongUserId());
                                userAuthScheduled.setPushArticleAuth(Boolean.TRUE);
                                LocalDateTime now = LocalDateTime.now().withSecond(0);
                                LocalDateTime endTime = now.plusMinutes(dto.getBanMinute()).withSecond(0);
                                userAuthScheduled.setStartTime(now);
                                userAuthScheduled.setEndTime(endTime);
                                userAuthScheduledMapper.insert(userAuthScheduled);
                            }
                        }
                    } else if (ReportHandleEnum.ARTICLE_BAN_UPLOAD.getHandleNo().equals(handleNo)) {
                        // 禁止传文件
                        BeanUtils.copyProperties(dto, handle);
                        handle.setReportId(reportId);
                        handle.setReportStatus(1);
                        handle.setReportHandleResult(ReportHandleEnum.ARTICLE_BAN_UPLOAD.getHandleDesc() + dto.getBanMinute() + "分钟");
                        handle.setReportHandleTime(LocalDateTime.now());
                        handle.setReportHandleAdmin(userByToken.getId());
                        int update = reportMapper.updateById(handle);
                        if (update == 1) {
                            Integer articleId = report.getReportTargetId();
                            Article article = articleMapper.selectById(articleId);
                            if (!ObjectUtils.isEmpty(article)) {
                                // 禁止用户上传文件权限
                                UserAuth userAuth = new UserAuth();
                                userAuth.setUserId(article.getArticleBelongUserId());
                                userAuth.setUploadAuth(Boolean.FALSE);
                                userAuthMapper.updateById(userAuth);

                                UserAuthScheduled userAuthScheduled = new UserAuthScheduled();
                                userAuthScheduled.setUserId(article.getArticleBelongUserId());
                                userAuthScheduled.setUploadAuth(Boolean.TRUE);
                                LocalDateTime now = LocalDateTime.now().withSecond(0);
                                LocalDateTime endTime = now.plusMinutes(dto.getBanMinute()).withSecond(0);
                                userAuthScheduled.setStartTime(now);
                                userAuthScheduled.setEndTime(endTime);
                                userAuthScheduledMapper.insert(userAuthScheduled);
                            }
                        }
                    } else {
                        // 暂不支持
                        result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",举报处理类型暂不支持");
                    }
                } else if (reportTarget == 2) {
                    // 评论
                    Report handle = new Report();
                    if (ReportHandleEnum.COMMENT_NO_RESULT.getHandleNo().equals(handleNo)){
                        // 举报不实
                        BeanUtils.copyProperties(dto, handle);
                        // 举报不实
                        handle.setReportId(reportId);
                        handle.setReportStatus(1);
                        handle.setReportHandleResult(ReportHandleEnum.COMMENT_NO_RESULT.getHandleDesc());
                        handle.setReportHandleTime(LocalDateTime.now());
                        handle.setReportHandleAdmin(userByToken.getId());
                        int update = reportMapper.updateById(handle);
                        if (update <= 0) {
                            result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",处理失败");
                        }
                    } else if (ReportHandleEnum.COMMENT_DELETE.getHandleNo().equals(handleNo)) {
                        // 删除
                        BeanUtils.copyProperties(dto, handle);
                        handle.setReportId(reportId);
                        handle.setReportStatus(1);
                        handle.setReportHandleResult(ReportHandleEnum.COMMENT_DELETE.getHandleDesc());
                        handle.setReportHandleTime(LocalDateTime.now());
                        handle.setReportHandleAdmin(userByToken.getId());
                        int update = reportMapper.updateById(handle);
                        if (update <= 0) {
                            result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",处理失败");
                        } else {
                            // 删除评论
                            Integer commentId = report.getReportTargetId();
                            ArticleComment comment = articleCommentMapper.selectById(commentId);
                            if (!ObjectUtils.isEmpty(comment)) {
                                articleCommentMapper.deleteById(commentId);
                            }
                        }
                    } else if (ReportHandleEnum.COMMENT_DELETE_AND_BAN_COMMENT_LINSHI.getHandleNo().equals(handleNo)) {
                        // 删除源文件,禁止发布文章
                        BeanUtils.copyProperties(dto, handle);
                        handle.setReportId(reportId);
                        handle.setReportStatus(1);
                        handle.setReportHandleResult(ReportHandleEnum.COMMENT_DELETE_AND_BAN_COMMENT_LINSHI.getHandleDesc() + dto.getBanMinute() + "分钟");
                        handle.setReportHandleTime(LocalDateTime.now());
                        handle.setReportHandleAdmin(userByToken.getId());
                        int update = reportMapper.updateById(handle);
                        if (update == 1) {
                            // 删除
                            Integer commentId = report.getReportTargetId();
                            ArticleComment comment = articleCommentMapper.selectById(commentId);
                            if (!ObjectUtils.isEmpty(comment)) {
                                articleCommentMapper.deleteById(commentId);

                                // 禁止用户发布权限
                                UserAuth userAuth = new UserAuth();
                                userAuth.setUserId(comment.getCommentFrom());
                                userAuth.setCommentAuth(Boolean.FALSE);
                                userAuthMapper.updateById(userAuth);

                                UserAuthScheduled userAuthScheduled = new UserAuthScheduled();
                                userAuthScheduled.setUserId(comment.getCommentFrom());
                                userAuthScheduled.setCommentAuth(Boolean.TRUE);
                                LocalDateTime now = LocalDateTime.now().withSecond(0);
                                LocalDateTime endTime = now.plusMinutes(dto.getBanMinute()).withSecond(0);
                                userAuthScheduled.setStartTime(now);
                                userAuthScheduled.setEndTime(endTime);
                                userAuthScheduledMapper.insert(userAuthScheduled);
                            }
                        }
                    } else if (ReportHandleEnum.COMMENT_BAN_COMMENT.getHandleNo().equals(handleNo)) {
                        // 禁止发言
                        BeanUtils.copyProperties(dto, handle);
                        handle.setReportId(reportId);
                        handle.setReportStatus(1);
                        handle.setReportHandleResult(ReportHandleEnum.COMMENT_BAN_COMMENT.getHandleDesc() + dto.getBanMinute() + "分钟");
                        handle.setReportHandleTime(LocalDateTime.now());
                        handle.setReportHandleAdmin(userByToken.getId());
                        int update = reportMapper.updateById(handle);
                        if (update == 1) {
                            Integer commentId = report.getReportTargetId();
                            ArticleComment comment = articleCommentMapper.selectById(commentId);
                            if (!ObjectUtils.isEmpty(comment)) {
                                // 禁止用户上传文件权限
                                UserAuth userAuth = new UserAuth();
                                userAuth.setUserId(comment.getCommentFrom());
                                userAuth.setCommentAuth(Boolean.FALSE);
                                userAuthMapper.updateById(userAuth);

                                UserAuthScheduled userAuthScheduled = new UserAuthScheduled();
                                userAuthScheduled.setUserId(comment.getCommentFrom());
                                userAuthScheduled.setCommentAuth(Boolean.TRUE);
                                LocalDateTime now = LocalDateTime.now().withSecond(0);
                                LocalDateTime endTime = now.plusMinutes(dto.getBanMinute()).withSecond(0);
                                userAuthScheduled.setStartTime(now);
                                userAuthScheduled.setEndTime(endTime);
                                userAuthScheduledMapper.insert(userAuthScheduled);
                            }
                        }
                    } else {
                        // 暂不支持
                        result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",举报处理类型暂不支持");
                    }
                }
                Integer emailFlag = dto.getEmailFlag();
                Report reply = new Report();
                BeanUtils.copyProperties(dto, reply);
                reply.setReportHandleAdmin(userByToken.getId());
                reply.setReportHandleTime(LocalDateTime.now());
                reply.setReportStatus(1);
                int insert = reportMapper.updateById(reply);
                if (insert == 1) {
                    if (emailFlag == 1) {
                        // 发邮件
                        String email = "";
                        User byId = userService.getById(reply.getReportUserId());
                        if (!ObjectUtils.isEmpty(byId) && StringUtils.hasLength(byId.getEmail())) {
                            email = byId.getEmail();
                        }
                        if (StringUtils.hasLength(email)) {
                            sendMailUtil.sendMail(email, "举报处理通知", sendMailUtil.buildReportContent(report, reply));
                        }
                    }
                } else {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",处理举报失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员处理举报失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员处理举报接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    /**
     * 删除文章及所属资源
     * @param articleId
     * @param fileId
     */
    private void deleteArticleInfo(Integer articleId, String fileId) {
        articleMapper.deleteById(articleId);
        if (StringUtils.hasLength(fileId)) {
            FilePath filePath = filePathMapper.selectById(fileId);
            if (!ObjectUtils.isEmpty(filePath)) {
                filePathMapper.deleteById(fileId);
                File file = new File(filePath.getFileRealPath());
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    @Override
    public void adminRemoveReport(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Report report = reportMapper.selectById(id);
            if (ObjectUtils.isEmpty(report) ) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",举报信息不存在");
            } else {
                int res = reportMapper.deleteById(id);
                if (res <= 0) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",删除失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员删除举报失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员删除举报接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, id, result);
        }
    }

    @Override
    public void adminGetReportInfo(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Report report = reportMapper.selectById(id);
            if (ObjectUtils.isEmpty(report)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",举报信息不存在");
            } else {
                AdminGetFeedBackInfoVO vo = new AdminGetFeedBackInfoVO();
                BeanUtils.copyProperties(report, vo);
                String images = report.getReportImages();
                if (StringUtils.hasLength(images)) {
                    vo.setReportImages(Arrays.asList(images.split(",")));
                }
                Integer reportTarget = report.getReportTarget();
                if (reportTarget == 1) {
                    BaseResult articleResult = BaseResult.ok();
                    articleService.adminGetArticleInfo(report.getReportTargetId(), request, articleResult);
                    if (articleResult.resOk()) {
                        vo.setArticleInfoVO(JSON.parseObject(JSON.toJSONString(articleResult.getData()), AdminArticleInfoVO.class));
                    }
                } else if (reportTarget == 2) {
                    BaseResult commentResult = BaseResult.ok();
                    articleCommentService.adminGetCommentInfo(report.getReportTargetId(), request, commentResult);
                    if (commentResult.resOk()) {
                        vo.setCommentVO(JSON.parseObject(JSON.toJSONString(commentResult.getData()), AdminCommentVO.class));
                    }
                }
                result.setData(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员获取用户举报详情失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取用户举报详情】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, id, result);
        }
    }

    @Override
    public void adminGetHandleType(Integer handleType, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            List<GetReportHandleTypeVO> reportHandleTypeVOList = ReportHandleEnum.getReportHandleTypeVOList(handleType);
            result.setData(reportHandleTypeVOList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("管理员获取举报类型失败：" + e.getMessage());
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取举报类型接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, handleType, result);
        }
    }
}
