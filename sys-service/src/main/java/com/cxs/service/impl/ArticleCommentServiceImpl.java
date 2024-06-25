package com.cxs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BasePageBean;
import com.cxs.base.BaseRequest;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.dto.KeyWordSearchPageDTO;
import com.cxs.dto.article.comment.GetArticleCommentListDTO;
import com.cxs.dto.article.comment.OperaCommentDTO;
import com.cxs.dto.article.comment.PublishCommentDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.mapper.ArticleCommentLikeMapper;
import com.cxs.mapper.ArticleMapper;
import com.cxs.mapper.UserAuthMapper;
import com.cxs.mapper.UserMapper;
import com.cxs.model.Article;
import com.cxs.model.ArticleComment;
import com.cxs.model.ArticleCommentLike;
import com.cxs.model.ArticleDraft;
import com.cxs.model.User;
import com.cxs.model.UserAuth;
import com.cxs.service.ArticleCommentService;
import com.cxs.mapper.ArticleCommentMapper;
import com.cxs.service.UserService;
import com.cxs.utils.DateUtil;
import com.cxs.utils.TimeUtil;
import com.cxs.vo.admin.comment.AdminCommentVO;
import com.cxs.vo.article.ArticleDraftVO;
import com.cxs.vo.comment.ArticleCommentVO;
import com.cxs.vo.user.SimpleUserVO;
import com.cxs.vo.user.UserCommentListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment> implements ArticleCommentService{

    @Autowired
    private ArticleCommentMapper articleCommentMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleCommentLikeMapper articleCommentLikeMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Override
    public void getMyCommentList(KeyWordSearchPageDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            BasePageBean pageBean = new BasePageBean();
            Page<ArticleComment> page = new Page<>(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<ArticleComment> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasLength(dto.getKeyWord())) {
                queryWrapper.like(ArticleComment::getCommentContent, dto.getKeyWord());
            }
            queryWrapper.eq(ArticleComment::getCommentFrom, userByToken.getId()).orderByDesc(ArticleComment::getCommentTime);
            IPage<ArticleComment> iPage = articleCommentMapper.selectPage(page, queryWrapper);
            List<ArticleComment> records = iPage.getRecords();
            List<UserCommentListVO> voList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(records)) {
                Set<String> toUserIds = records.stream().map(ArticleComment::getCommentTo).collect(Collectors.toSet());
                Set<Integer> articleIds = records.stream().map(ArticleComment::getArticleId).collect(Collectors.toSet());
                // 查询用户
                LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userLambdaQueryWrapper.select(User::getUserId, User::getNickName, User::getAvatar).in(User::getUserId, toUserIds);
                List<User> userList = userMapper.selectList(userLambdaQueryWrapper);
                Map<String, User> userMap = CollectionUtils.isEmpty(userList) ? new HashMap<>() :
                        userList.stream().collect(Collectors.toMap(User::getUserId, Function.identity(), (o1, o2) -> o1));
                // 查询文章
                LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
                articleLambdaQueryWrapper.select(Article::getArticleId, Article::getArticleTitle).eq(Article::getArticleStatus, 1).in(Article::getArticleId, articleIds);
                List<Article> articleList = articleMapper.selectList(articleLambdaQueryWrapper);
                Map<Integer, Article> articleMap = CollectionUtils.isEmpty(articleList) ? new HashMap<>() :
                        articleList.stream().collect(Collectors.toMap(Article::getArticleId, Function.identity(), (o1, o2) -> o1));
                for (ArticleComment record : records) {
                    UserCommentListVO vo = new UserCommentListVO();
                    BeanUtils.copyProperties(record, vo);
                    User user = userMap.get(record.getCommentTo());
                    if (!ObjectUtils.isEmpty(user)) {
                        SimpleUserVO userVO = new SimpleUserVO();
                        BeanUtils.copyProperties(user, userVO);
                        vo.setCommentTo(userVO);
                    }
                    Article article = articleMap.get(record.getArticleId());
                    if (!ObjectUtils.isEmpty(article)) {
                        UserCommentListVO.SimpleArticleVO articleVO = new UserCommentListVO.SimpleArticleVO();
                        BeanUtils.copyProperties(article, articleVO);
                        vo.setArticle(articleVO);
                    }
                    voList.add(vo);
                }
            }
            pageBean.setPageNum(iPage.getCurrent());
            pageBean.setPageSize(iPage.getSize());
            pageBean.setPages(iPage.getPages());
            pageBean.setTotal(iPage.getTotal());
            pageBean.setData(voList);
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("用户中心获取我的评论列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户中心获取我的评论列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void deleteComment(Integer commentId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            ArticleComment comment = articleCommentMapper.selectById(commentId);
            if (ObjectUtils.isEmpty(comment)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该评论不存在");
            } else {
                if (userByToken.getId().equals(comment.getCommentFrom())) {
                    // 直接删除
                    articleCommentMapper.deleteById(commentId);
                } else {
                    Integer articleId = comment.getArticleId();
                    Article article = articleMapper.selectById(articleId);
                    if (!ObjectUtils.isEmpty(article)) {
                        if (userByToken.getId().equals(article.getArticleBelongUserId())) {
                            // 直接删除
                            articleCommentMapper.deleteById(commentId);
                        } else {
                            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",仅支持删除本人评论及本人文章下的评论");
                        }
                    } else {
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",所属文章异常");
                    }
                }
            }
        } catch (Exception e) {
            log.error("删除评论失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户删除评论接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, commentId, result);
        }
    }

//    @Override
//    public void publishComment(PublishCommentDTO dto, HttpServletRequest request, BaseResult result) {
//        long startTime = System.currentTimeMillis();
//        try {
//            UserSubject userByToken = userService.getUserByToken(request);
//            String commentTo = dto.getCommentTo();
//            if (userByToken.getId().equals(commentTo)) {
//                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
//                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",不能自己回复自己");
//            } else {
//                UserAuth userAuth = userAuthMapper.selectById(userByToken.getId());
//                if (!userAuth.getCommentAuth()) {
//                    result.setCode(CurrencyErrorEnum.AUTH_LOCK.getCode());
//                    result.setMsg(CurrencyErrorEnum.AUTH_LOCK.getMsg() + ",暂无法进行评论,请联系管理员");
//                } else {
//                    ArticleComment comment = new ArticleComment();
//                    BeanUtils.copyProperties(dto, comment);
//                    comment.setCommentFrom(userByToken.getId());
//                    comment.setCommentTime(LocalDateTime.now());
//                    int insert = articleCommentMapper.insert(comment);
//                    if (insert != 1) {
//                        result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
//                        result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.error("用户评论失败,{}", e);
//            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
//            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
//        } finally {
//            long endTime = System.currentTimeMillis();
//            log.info("【{}】【用户评论接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, dto, result);
//        }
//    }

    @Override
    public void publishComment(PublishCommentDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);

            UserAuth userAuth = userAuthMapper.selectById(userByToken.getId());
            if (!userAuth.getCommentAuth()) {
                result.setCode(CurrencyErrorEnum.AUTH_LOCK.getCode());
                result.setMsg(CurrencyErrorEnum.AUTH_LOCK.getMsg() + ",暂无法进行评论,请联系管理员");
            } else {
                ArticleComment comment = new ArticleComment();
                BeanUtils.copyProperties(dto, comment);
                comment.setCommentFrom(userByToken.getId());
                comment.setCommentTime(LocalDateTime.now());
                int insert = articleCommentMapper.insert(comment);
                if (insert != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                }
            }
        } catch (Exception e) {
            log.error("用户评论失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户评论接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, dto, result);
        }
    }




    @Override
    public void getArticleCommentList(GetArticleCommentListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            BasePageBean pageBean = new BasePageBean();
            IPage<ArticleComment> page = new Page<>(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<ArticleComment> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ArticleComment::getArticleId, dto.getArticleId()).isNull(ArticleComment::getParentCommentId).orderByDesc(ArticleComment::getTop, ArticleComment::getCommentTime);
            IPage<ArticleComment> iPage = articleCommentMapper.selectPage(page, queryWrapper);
            List<ArticleComment> records = iPage.getRecords();
            List<ArticleCommentVO> voList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(records)) {
                voList = records.parallelStream().map(r -> {
                    ArticleCommentVO vo = new ArticleCommentVO();
                    BeanUtils.copyProperties(r, vo);
                    // 处理用户点赞状态、总的点赞数
                    if(!ObjectUtils.isEmpty(userByToken)) {
                        LambdaQueryWrapper<ArticleCommentLike> likeQueryWrapper = new LambdaQueryWrapper<>();
                        likeQueryWrapper.eq(ArticleCommentLike::getUserId, userByToken.getId()).eq(ArticleCommentLike::getCommentId, r.getCommentId()).select(ArticleCommentLike::getId);
                        List<ArticleCommentLike> articleCommentLikes = articleCommentLikeMapper.selectList(likeQueryWrapper);
                        vo.setLikeStatus((!CollectionUtils.isEmpty(articleCommentLikes) && articleCommentLikes.size() == 1));
                    }
                    // 点赞数
                    LambdaQueryWrapper<ArticleCommentLike> countQueryWrapper = new LambdaQueryWrapper<>();
                    countQueryWrapper.eq(ArticleCommentLike::getCommentId, r.getCommentId());
                    vo.setLikeCount(articleCommentLikeMapper.selectCount(countQueryWrapper));
                    // 时间格式化
                    vo.setCommentTimeFormat(TimeUtil.formatDefault(r.getCommentTime()));
                    List<String> userIds = new ArrayList<>(2);
                    userIds.add(r.getCommentTo());
                    userIds.add(r.getCommentFrom());
                    LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    userLambdaQueryWrapper.in(User::getUserId, userIds).select(User::getUserId, User::getNickName, User::getAvatar);
                    List<User> userList = userMapper.selectList(userLambdaQueryWrapper);
                    Map<String, User> userMap = userList.stream().collect(Collectors.toMap(User::getUserId, Function.identity(), (o1, o2) -> o1));
                    SimpleUserVO userFromVO = new SimpleUserVO();
                    User fromUser = userMap.get(r.getCommentFrom());
                    if (!ObjectUtils.isEmpty(fromUser)) {
                        BeanUtils.copyProperties(fromUser, userFromVO);
                        vo.setCommentFrom(userFromVO);
                    }

                    SimpleUserVO userToVO = new SimpleUserVO();
                    User toUser = userMap.get(r.getCommentTo());
                    if (!ObjectUtils.isEmpty(toUser)) {
                        BeanUtils.copyProperties(toUser, userToVO);
                        vo.setCommentTo(userToVO);
                    }
                    return vo;
                }).collect(Collectors.toList());

                voList.parallelStream().forEach(s -> {
                    LambdaQueryWrapper<ArticleComment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    commentLambdaQueryWrapper.eq(ArticleComment::getParentCommentId, s.getCommentId()).orderByDesc(ArticleComment::getTop).orderByAsc(ArticleComment::getCommentTime);
                    List<ArticleComment> articleComments = articleCommentMapper.selectList(commentLambdaQueryWrapper);
                    List<ArticleCommentVO.ArticleCommentChildVO> childVOList = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(articleComments)) {
                        childVOList = articleComments.parallelStream().map(r -> {
                            ArticleCommentVO.ArticleCommentChildVO vo = new ArticleCommentVO.ArticleCommentChildVO();
                            BeanUtils.copyProperties(r, vo);
                            if (!ObjectUtils.isEmpty(userByToken)) {
                                // 处理用户点赞状态、总的点赞数
                                LambdaQueryWrapper<ArticleCommentLike> likeQueryWrapper = new LambdaQueryWrapper<>();
                                likeQueryWrapper.eq(ArticleCommentLike::getUserId, userByToken.getId()).eq(ArticleCommentLike::getCommentId, r.getCommentId()).select(ArticleCommentLike::getId);
                                List<ArticleCommentLike> articleCommentLikes = articleCommentLikeMapper.selectList(likeQueryWrapper);
                                vo.setLikeStatus((!CollectionUtils.isEmpty(articleCommentLikes) && articleCommentLikes.size() == 1));
                            }
                            // 点赞数
                            LambdaQueryWrapper<ArticleCommentLike> countQueryWrapper = new LambdaQueryWrapper<>();
                            countQueryWrapper.eq(ArticleCommentLike::getCommentId, r.getCommentId());
                            vo.setLikeCount(articleCommentLikeMapper.selectCount(countQueryWrapper));
                            // 时间格式化
                            vo.setCommentTimeFormat(TimeUtil.formatDefault(r.getCommentTime()));

                            List<String> userIds = new ArrayList<>(2);
                            userIds.add(r.getCommentTo());
                            userIds.add(r.getCommentFrom());
                            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
                            userLambdaQueryWrapper.in(User::getUserId, userIds).select(User::getUserId, User::getNickName, User::getAvatar);
                            List<User> userList = userMapper.selectList(userLambdaQueryWrapper);
                            Map<String, User> userMap = userList.stream().collect(Collectors.toMap(User::getUserId, Function.identity(), (o1, o2) -> o1));
                            SimpleUserVO userFromVO = new SimpleUserVO();
                            User fromUser = userMap.get(r.getCommentFrom());
                            if (!ObjectUtils.isEmpty(fromUser)) {
                                BeanUtils.copyProperties(fromUser, userFromVO);
                                vo.setCommentFrom(userFromVO);
                            }

                            SimpleUserVO userToVO = new SimpleUserVO();
                            User toUser = userMap.get(r.getCommentTo());
                            if (!ObjectUtils.isEmpty(toUser)) {
                                BeanUtils.copyProperties(toUser, userToVO);
                                vo.setCommentTo(userToVO);
                            }
                            return vo;
                        }).collect(Collectors.toList());
                    }
                    s.setChildren(childVOList);
                });
            }
            pageBean.setPageNum(iPage.getCurrent());
            pageBean.setPageSize(iPage.getSize());
            pageBean.setPages(iPage.getPages());
            pageBean.setTotal(iPage.getTotal());
            pageBean.setData(voList);
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("获取评论列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取评论接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    @Override
    public void operaComment(OperaCommentDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Integer commentId = dto.getCommentId();
            ArticleComment comment = articleCommentMapper.selectById(commentId);
            if (ObjectUtils.isEmpty(comment)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",评论不存在");
            } else {
                Integer articleId = comment.getArticleId();
                Article article = articleMapper.selectById(articleId);
                if (ObjectUtils.isEmpty(article) || !article.getArticleStatus().equals(1)) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",原文章已删除或状态异常");
                } else {
                    UserSubject userByToken = userService.getUserByToken(request);
                    if (userByToken.getId().equals(article.getArticleBelongUserId())) {
                        ArticleComment articleComment = new ArticleComment();
                        BeanUtils.copyProperties(dto, articleComment);
                        int update = articleCommentMapper.updateById(articleComment);
                        if (update != 1) {
                            result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                        }
                    } else {
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",权限不足,仅能操作自己文章下的评论");
                    }
                }
            }
        } catch (Exception e) {
            log.error("用户评论操作失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户评论操作接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void adminGetCommentInfo(Integer commentId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            ArticleComment comment = articleCommentMapper.selectById(commentId);
            if (ObjectUtils.isEmpty(comment)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",评论不存在");
                return;
            }
            AdminCommentVO vo = new AdminCommentVO();
            BeanUtils.copyProperties(comment, vo);

            LambdaQueryWrapper<ArticleComment> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ArticleComment::getParentCommentId, commentId).orderByDesc(ArticleComment::getCommentTime);
            List<ArticleComment> articleComments = articleCommentMapper.selectList(queryWrapper);
            List<AdminCommentVO> childList = CollectionUtils.isEmpty(articleComments) ? new ArrayList<>(0) : articleComments.stream().map(r -> {
                AdminCommentVO v = new AdminCommentVO();
                BeanUtils.copyProperties(r, v);
                return v;
            }).collect(Collectors.toList());
            vo.setChildCommentList(childList);
            result.setData(vo);
        } catch (Exception e) {
            log.error("管理员获取评论详情失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取评论详情接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, commentId, result);
        }
    }
}




