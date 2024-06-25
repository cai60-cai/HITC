package com.cxs.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BasePageBean;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.bo.SystemRecommendArticleBO;
import com.cxs.bo.UserArticleListBo;
import com.cxs.config.CommonConfig;
import com.cxs.constant.CachePrefixContent;
import com.cxs.constant.CommonContent;
import com.cxs.dto.admin.article.AdminGetArticleListDTO;
import com.cxs.dto.admin.article.AdminUpdateArticleDTO;
import com.cxs.dto.admin.article.GetReviewedListDTO;
import com.cxs.dto.admin.article.ReviewedArticleDTO;
import com.cxs.dto.article.DownloadFileDTO;
import com.cxs.dto.article.GetArticleListDTO;
import com.cxs.dto.article.SaveOrUpdateArticleDTO;
import com.cxs.dto.article.SystemRecommendArticleDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.mapper.ArticleCollectionMapper;
import com.cxs.mapper.ArticleCommentMapper;
import com.cxs.mapper.ArticleDraftMapper;
import com.cxs.mapper.ArticleLikeMapper;
import com.cxs.mapper.ArticleReadMapper;
import com.cxs.mapper.ArticleSearchFlowMapper;
import com.cxs.mapper.OrderMapper;
import com.cxs.mapper.PointTradeFlowMapper;
import com.cxs.mapper.TagMapper;
import com.cxs.mapper.TechnologyTypeMapper;
import com.cxs.mapper.UserAuthMapper;
import com.cxs.mapper.UserMapper;
import com.cxs.mapper.UserRewardMapper;
import com.cxs.mapper.UserSettingMapper;
import com.cxs.model.Article;
import com.cxs.model.ArticleCollection;
import com.cxs.model.ArticleComment;
import com.cxs.model.ArticleLike;
import com.cxs.model.ArticleRead;
import com.cxs.model.ArticleSearchFlow;
import com.cxs.model.ArticleTag;
import com.cxs.model.FilePath;
import com.cxs.model.Order;
import com.cxs.model.PointTradeFlow;
import com.cxs.model.SystemMessage;
import com.cxs.model.Tag;
import com.cxs.model.TechnologyType;
import com.cxs.model.User;
import com.cxs.model.UserAuth;
import com.cxs.model.UserReward;
import com.cxs.model.UserSetting;
import com.cxs.service.ArticleService;
import com.cxs.mapper.ArticleMapper;
import com.cxs.service.ArticleTagService;
import com.cxs.service.BaseService;
import com.cxs.config.CompletableFutureService;
import com.cxs.service.FilePathService;
import com.cxs.service.SystemMessageService;
import com.cxs.service.UserService;
import com.cxs.utils.RedisUtil;
import com.cxs.utils.TimeUtil;
import com.cxs.vo.admin.article.AdminArticleInfoVO;
import com.cxs.vo.admin.article.ArticleReviewedListVO;
import com.cxs.vo.article.ArticleHotSearchVO;
import com.cxs.vo.article.ArticleInfoUserVO;
import com.cxs.vo.article.DownloadBeforeValidVO;
import com.cxs.vo.article.FileVO;
import com.cxs.vo.article.SystemRecommendArticleVO;
import com.cxs.vo.article.UserArticleInfoVO;
import com.cxs.vo.article.UserArticleListVO;
import com.cxs.vo.article.UserSelfArticleDetailVO;
import com.cxs.vo.article.UserUpdateArticleInfoVO;
import com.cxs.vo.file.FileDownloadVO;
import com.cxs.vo.tag.TagVO;
import com.cxs.vo.technology.TechnologyTypeVO;
import com.cxs.vo.user.UserProfileVO;
import com.cxs.vo.user.UserRewardVO;
import com.cxs.vo.user.UserSettingVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private BaseService baseService;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private ArticleDraftMapper articleDraftMapper;

    @Autowired
    private CompletableFutureService completableFutureService;

    @Autowired
    private FilePathService filePathService;

    @Autowired
    private ArticleReadMapper articleReadMapper;

    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    @Autowired
    private ArticleCollectionMapper articleCollectionMapper;

    @Autowired
    private TechnologyTypeMapper technologyTypeMapper;

    @Autowired
    private ArticleSearchFlowMapper articleSearchFlowMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private UserSettingMapper userSettingMapper;

    @Autowired
    private UserRewardMapper userRewardMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private ArticleCommentMapper articleCommentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PointTradeFlowMapper pointTradeFlowMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SystemMessageService systemMessageService;


    @Override
    @Transactional
    public void saveArticle(SaveOrUpdateArticleDTO dto, MultipartFile file, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            // 获取用户权限
            UserAuth userAuth = userAuthMapper.selectById(userByToken.getId());
            if (!userAuth.getPushArticleAuth()) {
                result.setCode(CurrencyErrorEnum.AUTH_LOCK.getCode());
                result.setMsg(CurrencyErrorEnum.AUTH_LOCK.getMsg() + ",暂无法进行文章发布,请联系管理员");
            } else {
                BaseResult upload = BaseResult.ok();
                Article article = new Article();
                BeanUtils.copyProperties(dto, article);
                // 上传文件
                if (!ObjectUtils.isEmpty(file)) {
                    baseService.fileUpload(file, request, upload);
                    if (upload.resOk()) {
                        FilePath filePath = (FilePath) upload.getData();
                        article.setArticleFileId(filePath.getFileId());
                    } else {
                        log.error("发布文章-上传文件, {}", upload.getMsg());
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章发布失败");
                    }
                }
                article.setCreateTime(LocalDateTime.now());
                article.setArticleBelongUserId(userByToken.getId());
                int insert = articleMapper.insert(article);
                if (insert == 1) {
                    if (StringUtils.hasLength(dto.getTags())) {
                        List<ArticleTag> articleTags = new ArrayList<>();
                        for (String s : dto.getTags().split(",")) {
                            ArticleTag tagItem = new ArticleTag();
                            tagItem.setArticleId(article.getArticleId());
                            tagItem.setTagId(Integer.parseInt(s));
                            articleTags.add(tagItem);
                        }
                        boolean save = articleTagService.saveBatch(articleTags);
                        if (save) {
                            // 删除草稿
                            completableFutureService.runAsyncTask(() -> {
                                // 发送消息
                                String msg = "您好,您发布的文章“" + article.getArticleTitle() + "”已进入审核流程，审核通过后会+1积分,请大家理解和支持";
                                SystemMessage message = new SystemMessage();
                                message.setReceiveUserId(userByToken.getId());
                                message.setCreateTime(LocalDateTime.now());
                                message.setMessageContent(msg);
                                systemMessageService.saveSysMessage(message, true, userByToken.getId(), BaseResult.ok());
                                // 删除草稿
                                articleDraftMapper.deleteById(userByToken.getId());
                            });
                        } else {
                            result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",文章发布失败");
                        }
                    }
                } else {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",文章发布失败");
                }
            }
        } catch (Exception e) {
            log.error("用户发布文章失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户发布文章接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, dto, result);
        }
    }

    @Override
    @Transactional
    public void updateArticle(SaveOrUpdateArticleDTO dto, MultipartFile file, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            Article article = articleMapper.selectById(dto.getArticleId());
            if (ObjectUtils.isEmpty(article)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章不存在");
            } else {
                if (article.getArticleStatus() != null && article.getArticleStatus().equals(1)) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",已发布的文章不允许修改");
                } else {
                    // 删除原标签
                    QueryWrapper<ArticleTag> wrapper = new QueryWrapper<>();
                    wrapper.eq("article_id", dto.getArticleId());
                    articleTagService.remove(wrapper);
                    Article updateArticle = new Article();
                    BeanUtils.copyProperties(dto, updateArticle);

                    Integer fileFlag = dto.getFileFlag();
                    if (ObjectUtils.isEmpty(fileFlag)) {
                        fileFlag = 0;
                    }
                    if (fileFlag.equals(1)) {
                        if (!ObjectUtils.isEmpty(file)) {
                            BaseResult upload = BaseResult.ok();
                            baseService.fileUpload(file, request, upload);
                            if (upload.resOk()) {
                                FilePath filePath = (FilePath) upload.getData();
                                updateArticle.setArticleFileId(filePath.getFileId());
                            } else {
                                log.error("修改文章-上传文件, {}", upload.getMsg());
                                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章修改失败");
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                return;
                            }
                        } else {
                            updateArticle.setArticleFileId(null);
                        }
                    } else {
                        updateArticle.setArticleFileId(article.getArticleFileId());
                    }
                    updateArticle.setUpdateTime(LocalDateTime.now());
                    updateArticle.setArticleStatus(0);
                    int update = articleMapper.updateById(updateArticle);
                    if (update == 1) {
                        if (StringUtils.hasLength(dto.getTags())) {
                            List<ArticleTag> articleTags = new ArrayList<>();
                            for (String s : dto.getTags().split(",")) {
                                ArticleTag tagItem = new ArticleTag();
                                tagItem.setArticleId(article.getArticleId());
                                tagItem.setTagId(Integer.parseInt(s));
                                articleTags.add(tagItem);
                            }
                            boolean save = articleTagService.saveBatch(articleTags);
                            if (save) {
                                // 删除草稿
                                Integer finalFileFlag = fileFlag;
                                completableFutureService.runAsyncTask(() -> {
                                    // todo message

                                    // 删除草稿
                                    articleDraftMapper.deleteById(userByToken.getId());
                                    // 删除源文件
                                    if (finalFileFlag == 1 && StringUtils.hasLength(article.getArticleFileId())) {
                                        filePathService.removeFileByFileId(article.getArticleFileId(), new BaseResult());
                                    }
                                });
                            } else {
                                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",文章修改失败");
                            }
                        }
                    } else {
                        result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",文章修改失败");
                    }
                }
            }
        } catch (Exception e) {
            log.error("用户修改文章失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户修改文章接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void getArticleList(GetArticleListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            BasePageBean pageBean = null;
            TechnologyType technologyType = null;
            Tag tag = null;
            if (!ObjectUtils.isEmpty(dto.getTypeId())) {
                technologyType = technologyTypeMapper.selectById(dto.getTypeId());
            }
            if (!ObjectUtils.isEmpty(dto.getTagId())) {
                tag = tagMapper.selectById(dto.getTagId());
            }
            JSONObject other = new JSONObject();
            other.put("tags", dto.getTags());
            other.put("tag", tag);
            other.put("type", technologyType);
            // 从接口获取数据
            pageBean = getPageBeanFromHttp(dto);
            readStatusHandle(userByToken, pageBean);
            pageBean.setOther(other);
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("用户获取文章列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户获取文章列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    /**
     * 处理点赞数
     *
     * @param userByToken
     * @param pageBean
     */
    private void readStatusHandle(UserSubject userByToken, BasePageBean pageBean) {
        // 处理阅读数、阅读标记
        if (!ObjectUtils.isEmpty(pageBean.getData())) {
            List list = (List) pageBean.getData();
            if (!CollectionUtils.isEmpty(list)) {
                for (Object o : list) {
                    if (o instanceof JSONObject) {
                        JSONObject listVO = (JSONObject) o;
                        // 获取阅读标记
                        QueryWrapper<ArticleRead> countWrapper = new QueryWrapper<>();
                        countWrapper.eq("article_id", listVO.get("articleId"));
                        listVO.put("readNum", articleReadMapper.selectCount(countWrapper));
                        if (ObjectUtils.isEmpty(userByToken)) {
                            listVO.put("readFlag", Boolean.FALSE);
                        } else {
                            QueryWrapper<ArticleRead> flagWrapper = new QueryWrapper<>();
                            flagWrapper.eq("article_id", listVO.get("articleId"))
                                    .eq("user_id", userByToken.getId());
                            ArticleRead articleRead = articleReadMapper.selectOne(flagWrapper);
                            listVO.put("readFlag", ObjectUtils.isEmpty(articleRead) ? Boolean.FALSE : Boolean.TRUE);
                        }
                        // 处理时间
                        listVO.put("createTimeFormat", TimeUtil.formatDefault(listVO.getDate("createTime")));
                    } else if (o instanceof UserArticleListVO) {
                        UserArticleListVO listVO = (UserArticleListVO) o;
                        QueryWrapper<ArticleRead> countWrapper = new QueryWrapper<>();
                        countWrapper.eq("article_id", listVO.getArticleId());
                        listVO.setReadNum(articleReadMapper.selectCount(countWrapper));
                        if (ObjectUtils.isEmpty(userByToken)) {
                            listVO.setReadFlag(Boolean.FALSE);
                        } else {
                            QueryWrapper<ArticleRead> flagWrapper = new QueryWrapper<>();
                            flagWrapper.eq("article_id", listVO.getArticleId())
                                    .eq("user_id", userByToken.getId());
                            ArticleRead articleRead = articleReadMapper.selectOne(flagWrapper);
                            listVO.setReadFlag(ObjectUtils.isEmpty(articleRead) ? Boolean.FALSE : Boolean.TRUE);
                        }
                        // 处理时间
                        listVO.setCreateTimeFormat(TimeUtil.formatDefault(listVO.getCreateTime()));
                    }
                }
            }
        }
    }

    /**
     * 从接口获取首页数据列表
     *
     * @param dto
     * @return
     */
    private BasePageBean<List<UserArticleListVO>> getIndexArticlePageBeanFromHttp(GetArticleListDTO dto) {
        BasePageBean<List<UserArticleListVO>> pageBean = new BasePageBean<>();
        IPage<Article> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        IPage<UserArticleListBo> iPage = articleMapper.selectArticleList(page, dto.getKeyword(), dto.getTags(), dto.getTypeId(), dto.getTagId(), 1);
        pageBean.setKeyword(dto.getKeyword());
        pageBean.setPageNum(iPage.getCurrent());
        pageBean.setPageSize(iPage.getSize());
        pageBean.setPages(iPage.getPages());
        pageBean.setTotal(iPage.getTotal());
        List<UserArticleListVO> voList = CollectionUtils.isEmpty(iPage.getRecords()) ? new ArrayList<>(0) : iPage.getRecords().stream().map(r -> {
            UserArticleListVO vo = new UserArticleListVO();
            BeanUtils.copyProperties(r, vo);
            return vo;
        }).collect(Collectors.toList());
        pageBean.setData(voList);
        return pageBean;
    }

    /**
     * 从接口获取数据列表
     *
     * @param dto
     * @return
     */
    private BasePageBean getPageBeanFromHttp(GetArticleListDTO dto) {
        BasePageBean pageBean;
        IPage<Article> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        IPage<UserArticleListBo> iPage = articleMapper.selectArticleList(page, dto.getKeyword(), dto.getTags(), dto.getTypeId(), dto.getTagId(), 0);
        pageBean = BasePageBean.builder()
                .keyword(dto.getKeyword())
                .pageNum(iPage.getCurrent())
                .pageSize(iPage.getSize())
                .pages(iPage.getPages())
                .total(iPage.getTotal())
                .build();
        List<UserArticleListVO> voList = CollectionUtils.isEmpty(iPage.getRecords()) ? new ArrayList<>(0) : iPage.getRecords().stream().map(r -> {
            UserArticleListVO vo = new UserArticleListVO();
            BeanUtils.copyProperties(r, vo);
            return vo;
        }).collect(Collectors.toList());
        pageBean.setData(voList);
        return pageBean;
    }

    @Override
    public void getArticleInfo(Integer articleId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            Article article = articleMapper.selectById(articleId);
            if (ObjectUtils.isEmpty(article) || !article.getArticleStatus().equals(1)) {
                result.setData(null);
            } else {
                UserArticleInfoVO vo = new UserArticleInfoVO();
                BeanUtils.copyProperties(article, vo);
                TechnologyType technologyType = technologyTypeMapper.selectById(article.getArticleType());
                TechnologyTypeVO typeVO = new TechnologyTypeVO();
                BeanUtils.copyProperties(technologyType, typeVO);
                vo.setType(typeVO);
                // 评论人数
                LambdaQueryWrapper<ArticleComment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
                commentLambdaQueryWrapper.eq(ArticleComment::getArticleId, article.getArticleId());
                vo.setCommentNum(articleCommentMapper.selectCount(commentLambdaQueryWrapper));
                // 阅读人数
                LambdaQueryWrapper<ArticleRead> readNumWrapper = new LambdaQueryWrapper<>();
                readNumWrapper.eq(ArticleRead::getArticleId, article.getArticleId());
                vo.setReadNum(articleReadMapper.selectCount(readNumWrapper));
                // 点赞人数
                LambdaQueryWrapper<ArticleLike> likeNumWrapper = new LambdaQueryWrapper<>();
                likeNumWrapper.eq(ArticleLike::getArticleId, article.getArticleId());
                vo.setLikeNum(articleLikeMapper.selectCount(likeNumWrapper));
                // 收藏人数
                LambdaQueryWrapper<ArticleCollection> collectionNumWrapper = new LambdaQueryWrapper<>();
                collectionNumWrapper.eq(ArticleCollection::getArticleId, article.getArticleId());
                vo.setCollectionNum(articleCollectionMapper.selectCount(collectionNumWrapper));
                // 标签
                List<Tag> tags = tagMapper.selectTagListByArticleId(articleId);
                vo.setTags(CollectionUtils.isEmpty(tags) ? new ArrayList<>(0) : tags.stream().map(t -> {
                    TagVO tagVO = new TagVO();
                    BeanUtils.copyProperties(t, tagVO);
                    return tagVO;
                }).collect(Collectors.toList()));
                if (!ObjectUtils.isEmpty(userByToken)) {
                    // 阅读标记
                    LambdaQueryWrapper<ArticleRead> readFlagWrapper = new LambdaQueryWrapper<>();
                    readFlagWrapper.eq(ArticleRead::getArticleId, article.getArticleId())
                            .eq(ArticleRead::getUserId, userByToken.getId());
                    vo.setReadFlag(ObjectUtils.isEmpty(articleReadMapper.selectOne(readFlagWrapper)) ? Boolean.FALSE : Boolean.TRUE);

                    // 阅读标记
                    LambdaQueryWrapper<ArticleLike> likeFlagWrapper = new LambdaQueryWrapper<>();
                    likeFlagWrapper.eq(ArticleLike::getArticleId, article.getArticleId())
                            .eq(ArticleLike::getUserId, userByToken.getId());
                    vo.setLikeFlag(ObjectUtils.isEmpty(articleLikeMapper.selectOne(likeFlagWrapper)) ? Boolean.FALSE : Boolean.TRUE);

                    // 收藏标记
                    LambdaQueryWrapper<ArticleCollection> collectionFlagWrapper = new LambdaQueryWrapper<>();
                    collectionFlagWrapper.eq(ArticleCollection::getArticleId, article.getArticleId())
                            .eq(ArticleCollection::getUserId, userByToken.getId());
                    vo.setCollectionFlag(ObjectUtils.isEmpty(articleCollectionMapper.selectOne(collectionFlagWrapper)) ? Boolean.FALSE : Boolean.TRUE);
                }

                if (StringUtils.hasLength(article.getArticleFileId())) {
                    FilePath filePath = filePathService.getById(article.getArticleFileId());
                    if (!ObjectUtils.isEmpty(filePath)) {
                        FileVO fileVO = new FileVO();
                        BeanUtils.copyProperties(filePath, fileVO);
                        fileVO.setFileName(filePath.getFileOrignName());
                        fileVO.setFileStructure(JSON.parseArray(filePath.getFileStructure()));
                        vo.setFileInfo(fileVO);
                    }
                }

                // 处理文章用户相关信息
                ArticleInfoUserVO articleInfoUserVO = new ArticleInfoUserVO();
                User beLongUser = userService.getById(article.getArticleBelongUserId());
                if (!ObjectUtils.isEmpty(beLongUser)) {
                    BeanUtils.copyProperties(beLongUser, articleInfoUserVO);
                }
                LambdaQueryWrapper<Article> allArticleWrapper = new LambdaQueryWrapper<>();
                allArticleWrapper.eq(Article::getArticleBelongUserId, article.getArticleBelongUserId())
                        .eq(Article::getArticleStatus, 1);
                List<Article> articleList = articleMapper.selectList(allArticleWrapper);
                if (!CollectionUtils.isEmpty(articleList)) {
                    articleInfoUserVO.setArticleCount(articleList.size());
                    articleInfoUserVO.setSelfArticleCount(articleList.stream().filter(a -> a.getArticleIsSelf().equals(1)).count());
                }

                LambdaQueryWrapper<ArticleLike> beArticleLikeWrapper = new LambdaQueryWrapper<>();
                beArticleLikeWrapper.eq(ArticleLike::getBelongUserId, article.getArticleBelongUserId());
                articleInfoUserVO.setLikedCount(articleLikeMapper.selectCount(beArticleLikeWrapper));


                LambdaQueryWrapper<ArticleCollection> beArticleCollectionWrapper = new LambdaQueryWrapper<>();
                beArticleCollectionWrapper.eq(ArticleCollection::getUserId, article.getArticleBelongUserId());
                articleInfoUserVO.setCollectionCount(articleCollectionMapper.selectCount(beArticleCollectionWrapper));


                UserSetting userSetting = userSettingMapper.selectById(article.getArticleBelongUserId());
                UserSettingVO settingVO = new UserSettingVO();
                if (!ObjectUtils.isEmpty(userSetting)) {
                    BeanUtils.copyProperties(userSetting, settingVO);
                    articleInfoUserVO.setSettings(settingVO);
                }

                vo.setUserInfo(articleInfoUserVO);

                result.setData(vo);
            }
        } catch (Exception e) {
            log.error("用户获取文章详情失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户获取文章详情接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, articleId, result);
        }
    }

    @Override
    public void readArticle(Integer articleId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            if (ObjectUtils.isEmpty(userByToken)) {
                // 未登录
            } else {
                Article article = articleMapper.selectById(articleId);
                if (ObjectUtils.isEmpty(article)) {
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章不存在");
                } else {
                    LambdaQueryWrapper<ArticleRead> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(ArticleRead::getArticleId, articleId)
                            .eq(ArticleRead::getUserId, userByToken.getId());
                    Integer integer = articleReadMapper.selectCount(wrapper);
                    if (integer == 0) {
                        ArticleRead articleRead = new ArticleRead();
                        articleRead.setArticleId(articleId);
                        articleRead.setUserId(userByToken.getId());
                        articleRead.setReadTime(LocalDateTime.now());
                        int insert = articleReadMapper.insert(articleRead);
                        if (insert != 1) {
                            log.error(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",阅读文章失败");
                            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",阅读文章失败");
                        }
                    } else {
                        log.error(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",阅读文章失败");
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章已阅读");
                    }
                }
            }
        } catch (Exception e) {
            log.error("用户阅读文章失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户阅读文章接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, articleId, result);
        }
    }

    @Override
    public void likeArticle(Integer articleId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            if (ObjectUtils.isEmpty(userByToken)) {
                // 未登录
                result.setCode(CurrencyErrorEnum.UNAUTHORIZED.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户未登录");
            } else {
                Article article = articleMapper.selectById(articleId);
                if (ObjectUtils.isEmpty(article)) {
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章不存在");
                } else {
                    LambdaQueryWrapper<ArticleLike> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(ArticleLike::getArticleId, articleId)
                            .eq(ArticleLike::getUserId, userByToken.getId());
                    Integer integer = articleLikeMapper.selectCount(wrapper);
                    if (integer == 0) {
                        ArticleLike articleLike = new ArticleLike();
                        articleLike.setArticleId(articleId);
                        articleLike.setUserId(userByToken.getId());
                        articleLike.setBelongUserId(article.getArticleBelongUserId());
                        articleLike.setLikeTime(LocalDateTime.now());
                        int insert = articleLikeMapper.insert(articleLike);
                        if (insert != 1) {
                            log.error(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",点赞文章失败");
                            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",点赞文章失败");
                        } else {
                            completableFutureService.runAsyncTask(() -> {
                                // 发送消息
                                String msg = "您好,用户"+userByToken.getUsername()+"点赞了您发布的文章“" + article.getArticleTitle() + "”";
                                SystemMessage message = new SystemMessage();
                                message.setReceiveUserId(article.getArticleBelongUserId());
                                message.setCreateTime(LocalDateTime.now());
                                message.setMessageContent(msg);
                                systemMessageService.saveSysMessage(message, true, article.getArticleBelongUserId(), BaseResult.ok());
                            });
                        }
                    } else {
                        log.error(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",点赞文章失败");
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",已点赞的文章不能重复点赞");
                    }
                }
            }
        } catch (Exception e) {
            log.error("用户点赞文章失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户点赞文章接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, articleId, result);
        }
    }

    @Override
    public void collectionArticle(Integer articleId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            if (ObjectUtils.isEmpty(userByToken)) {
                // 未登录
                result.setCode(CurrencyErrorEnum.UNAUTHORIZED.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户未登录");
            } else {
                Article article = articleMapper.selectById(articleId);
                if (ObjectUtils.isEmpty(article)) {
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章不存在");
                } else {
                    LambdaQueryWrapper<ArticleCollection> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(ArticleCollection::getArticleId, articleId)
                            .eq(ArticleCollection::getUserId, userByToken.getId());
                    Integer integer = articleCollectionMapper.selectCount(wrapper);
                    if (integer == 0) {
                        ArticleCollection articleCollection = new ArticleCollection();
                        articleCollection.setArticleId(articleId);
                        articleCollection.setUserId(userByToken.getId());
                        articleCollection.setBelongUserId(article.getArticleBelongUserId());
                        articleCollection.setCollectionTime(LocalDateTime.now());
                        int insert = articleCollectionMapper.insert(articleCollection);
                        if (insert != 1) {
                            log.error(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",收藏文章失败");
                            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",收藏文章失败");
                        } else {
                            completableFutureService.runAsyncTask(() -> {
                                // 发送消息
                                String msg = "您好,用户"+userByToken.getUsername()+"收藏了您发布的文章“" + article.getArticleTitle() + "”";
                                SystemMessage message = new SystemMessage();
                                message.setReceiveUserId(article.getArticleBelongUserId());
                                message.setCreateTime(LocalDateTime.now());
                                message.setMessageContent(msg);
                                systemMessageService.saveSysMessage(message, true, article.getArticleBelongUserId(), BaseResult.ok());
                            });
                        }
                    } else {
                        log.error(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",收藏文章失败");
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",已收藏的文章不能重复收藏");
                    }
                }
            }
        } catch (Exception e) {
            log.error("用户收藏文章失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户收藏文章接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, articleId, result);
        }
    }

    @Override
    public void markArticleSearchLog(Integer articleId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Article article = articleMapper.selectById(articleId);
            if (ObjectUtils.isEmpty(article)) {
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章不存在");
            } else {
                ArticleSearchFlow articleSearchFlow = new ArticleSearchFlow();
                articleSearchFlow.setArticleId(articleId);
                articleSearchFlow.setSearchTime(LocalDateTime.now());
                int insert = articleSearchFlowMapper.insert(articleSearchFlow);
                if (insert != 1) {
                    log.error(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章搜索流水记录失败");
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章搜索流水记录失败");
                }
            }
        } catch (Exception e) {
            log.error("文章搜索流水记录失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【文章搜索流水记录接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, articleId, result);
        }
    }

    @Override
    public void cancelLikeArticle(Integer articleId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            if (ObjectUtils.isEmpty(userByToken)) {
                // 未登录
                result.setCode(CurrencyErrorEnum.UNAUTHORIZED.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户未登录");
            } else {
                Article article = articleMapper.selectById(articleId);
                if (ObjectUtils.isEmpty(article)) {
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章不存在");
                } else {
                    LambdaQueryWrapper<ArticleLike> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(ArticleLike::getArticleId, articleId)
                            .eq(ArticleLike::getUserId, userByToken.getId());
                    ArticleLike articleLike = articleLikeMapper.selectOne(wrapper);
                    if (!ObjectUtils.isEmpty(articleLike)) {
                        int delete = articleLikeMapper.deleteById(articleLike.getId());
                        if (delete != 1) {
                            log.error(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",取消点赞文章失败");
                            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",取消点赞文章失败");
                        }
                    } else {
                        log.error(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",取消点赞文章失败");
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户尚未点赞该文章");
                    }
                }
            }
        } catch (Exception e) {
            log.error("取消点赞文章失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【取消点赞文章接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, articleId, result);
        }
    }

    @Override
    public void cancelCollectionArticle(Integer articleId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            if (ObjectUtils.isEmpty(userByToken)) {
                // 未登录
                result.setCode(CurrencyErrorEnum.UNAUTHORIZED.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户未登录");
            } else {
                Article article = articleMapper.selectById(articleId);
                if (ObjectUtils.isEmpty(article)) {
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章不存在");
                } else {
                    LambdaQueryWrapper<ArticleCollection> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(ArticleCollection::getArticleId, articleId)
                            .eq(ArticleCollection::getUserId, userByToken.getId());
                    ArticleCollection articleCollection = articleCollectionMapper.selectOne(wrapper);
                    if (!ObjectUtils.isEmpty(articleCollection)) {
                        int delete = articleCollectionMapper.deleteById(articleCollection.getId());
                        if (delete != 1) {
                            log.error(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",取消收藏文章失败");
                            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",取消收藏文章失败");
                        }
                    } else {
                        log.error(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",取消收藏文章失败");
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户尚未收藏该文章");
                    }
                }
            }
        } catch (Exception e) {
            log.error("取消收藏文章失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【取消收藏文章接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, articleId, result);
        }
    }

    @Override
    public void getUserRewardInfo(Integer articleId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Article article = articleMapper.selectById(articleId);
            if (ObjectUtils.isEmpty(article) || !article.getArticleStatus().equals(1)) {
                result.setData(null);
            } else {
                UserSetting userSetting = userSettingMapper.selectById(article.getArticleBelongUserId());
                if (ObjectUtils.isEmpty(userSetting) || !userSetting.getOpenReward()) {
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户暂未开启打赏功能");
                } else {
                    UserReward userReward = userRewardMapper.selectById(article.getArticleBelongUserId());
                    if (!ObjectUtils.isEmpty(userReward)) {
                        UserRewardVO vo = new UserRewardVO();
                        BeanUtils.copyProperties(userReward, vo);
                        result.setData(vo);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取用户打赏信息失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户打赏信息接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, articleId, result);
        }
    }

    @Override
    public void downloadFileBase64(DownloadFileDTO dto, HttpServletRequest request, BaseResult result) {

        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            if (ObjectUtils.isEmpty(userByToken)) {
                // 未登录
                result.setCode(CurrencyErrorEnum.UNAUTHORIZED.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户未登录");

            } else {
                Article article = articleMapper.selectById(dto.getArticleId());
                if (ObjectUtils.isEmpty(article)) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章不存在");

                } else {
                    if (!StringUtils.hasLength(article.getArticleFileId())) {
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该文章未上传文件");

                    } else {
                        FilePath filePath = filePathService.getById(article.getArticleFileId());
                        if (!ObjectUtils.isEmpty(filePath)) {
                            String fileRealPath = filePath.getFileRealPath();
                            String fileOrignName = filePath.getFileOrignName();
                            File file = new File(fileRealPath);
                            if (!file.exists()) {
                                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文件不存在或已过期");

                            } else {
                                FileDownloadVO vo = new FileDownloadVO();
                                String fileEncoding = Base64Utils.encodeToString(FileUtil.readBytes(file));
                                vo.setFile(fileEncoding);
                                vo.setFileName(fileOrignName);
                                vo.setFileId(filePath.getFileId());
                                vo.setFileSize(filePath.getFileSize());
                                vo.setFilePostfix(fileOrignName.substring(fileOrignName.lastIndexOf(".")));
                                result.setData(vo);
                            }
                        } else {
                            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文件不存在或已过期");

                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("用户下载文件失败,{}", e);

            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户下载文件接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    @Override
    public void getSystemRecommendArticleList(SystemRecommendArticleDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            List<SystemRecommendArticleVO> voList = new ArrayList<>();
            if (dto.getPageNum().equals(-1)) {
                Object value = redisUtil.getValue(CachePrefixContent.INDEX_SYSTEM_RECOMMEND_LIST);
                if (!ObjectUtils.isEmpty(value)) {
                    voList = JSON.parseArray(JSON.toJSONString(value), SystemRecommendArticleVO.class);
                } else {
                    IPage<Article> page = new Page<>(1, commonConfig.getArticleSysRecommendlistNum());
                    List<SystemRecommendArticleBO> recommendArticleBOS = articleMapper.selectSystemRecommend(page);
                    voList = CollectionUtils.isEmpty(recommendArticleBOS) ? new ArrayList<>(0) : recommendArticleBOS.stream().map(r -> {
                        SystemRecommendArticleVO vo = new SystemRecommendArticleVO();
                        BeanUtils.copyProperties(r, vo);
                        return vo;
                    }).collect(Collectors.toList());
                    redisUtil.set(CachePrefixContent.INDEX_SYSTEM_RECOMMEND_LIST, voList, commonConfig.getCacheShortTime(), TimeUnit.MINUTES);
                }
                if (!CollectionUtils.isEmpty(voList)) {
                    for (SystemRecommendArticleVO articleVO : voList) {
                        LambdaQueryWrapper<ArticleRead> wrapper = new LambdaQueryWrapper<>();
                        wrapper.eq(ArticleRead::getArticleId, articleVO.getArticleId());
                        articleVO.setReadCount(articleReadMapper.selectCount(wrapper));
                    }
                }
                result.setData(voList);
            } else {
                // todo
            }
        } catch (Exception e) {
            log.error("获取官方推荐文章列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取官方推荐文章列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void getHotArticleList(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            List<ArticleHotSearchVO> voList = new ArrayList<>();
            Object value = redisUtil.getValue(CachePrefixContent.INDEX_ARTICLE_HOT_LIST);
            if (!ObjectUtils.isEmpty(value)) {
                voList = JSON.parseArray(JSON.toJSONString(value), ArticleHotSearchVO.class);
                if (!CollectionUtils.isEmpty(voList)) {
                    List<Integer> ids = voList.stream().map(ArticleHotSearchVO::getArticleId).collect(Collectors.toList());
                    LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
                    wrapper.in(Article::getArticleId, ids).select(Article::getArticleId, Article::getArticleTitle);
                    List<Article> articles = articleMapper.selectList(wrapper);
                    Map<Integer, Article> collect = articles.stream().collect(Collectors.toMap(Article::getArticleId, Function.identity(), (o1, o2) -> o1));
                    voList.forEach(vo -> {
                        vo.setArticleTitle(ObjectUtils.isEmpty(collect.get(vo.getArticleId())) ? "" : collect.get(vo.getArticleId()).getArticleTitle());
                    });
                }
            }
            result.setData(voList);
        } catch (Exception e) {
            log.error("获取热搜文章列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取热搜文章列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void downloadFile(DownloadFileDTO dto, HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        BaseResult result = BaseResult.ok();
        FileInputStream in = null;
        BufferedInputStream bin = null;
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            if (ObjectUtils.isEmpty(userByToken)) {
                // 未登录
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            } else {
                Article article = articleMapper.selectById(dto.getArticleId());
                if (ObjectUtils.isEmpty(article)) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章不存在");
                } else {
                    if (!StringUtils.hasLength(article.getArticleFileId())) {
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该文章未上传文件");
                    } else {
                        FilePath filePath = filePathService.getById(article.getArticleFileId());
                        if (!ObjectUtils.isEmpty(filePath)) {
                            userService.getPersonal(request, result);
                            UserProfileVO userProfileVO = (UserProfileVO) result.getData();
                            Integer point = userProfileVO.getPoint();
                            Integer articleDownloadPoint = article.getArticleDownloadPoint();
                            if (point >= articleDownloadPoint) {
                                String fileRealPath = filePath.getFileRealPath();
                                String fileOrignName = filePath.getFileOrignName();
                                File file = new File(fileRealPath);
                                if (!file.exists()) {
                                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文件不存在或已过期");
                                } else {
                                    response.setContentType("application/x-msdownload");
                                    String encode = URLEncoder.encode(fileOrignName, "utf-8");
                                    fileOrignName = new String(encode.getBytes("UTF-8"), "GBK");
                                    response.addHeader("Content-Disposition", "attachment;filename=" + fileOrignName);
                                    ServletOutputStream out = response.getOutputStream();
                                    in = new FileInputStream(file);
                                    bin = new BufferedInputStream(in);
                                    byte[] bytes = new byte[1024];
                                    int len;
                                    while ((len = bin.read(bytes)) != -1) {
                                        out.write(bytes, 0, len);
                                        out.flush();
                                    }
                                    out.flush();
                                    User user = new User();
                                    user.setUserId(userProfileVO.getUserId());
                                    user.setPoint(point - articleDownloadPoint);
                                    userService.updateById(user);
                                    return;
                                }
                            } else {
                                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                                result.setData(null);
                                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户积分不足，下载失败");
                            }
                        } else {
                            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文件不存在或已过期");
                        }
                    }
                }
            }
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(result));
            writer.flush();
        } catch (Exception e) {
            try {
                if (bin != null) {
                    bin.close();
                }
                if (in != null) {
                    in.close();
                }
                log.error("用户下载文件失败,{}", e);
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
                response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(result));
                writer.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户下载文件接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    @Override
    public void getReviewedList(GetReviewedListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            BasePageBean pageBean = new BasePageBean();
            Page<Article> page = new Page<>(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasLength(dto.getKeyWord())) {
                queryWrapper.like(Article::getArticleTitle, dto.getKeyWord());
            }
            if (!ObjectUtils.isEmpty(dto.getArticleType())) {
                queryWrapper.eq(Article::getArticleType, dto.getArticleType());
            }
            if (!ObjectUtils.isEmpty(dto.getArticleIsSelf())) {
                queryWrapper.eq(Article::getArticleIsSelf, dto.getArticleIsSelf());
            }
            queryWrapper.eq(Article::getArticleStatus, 0).select(Article::getArticleId, Article::getArticleTitle, Article::getArticleIsSelf, Article::getArticleType, Article::getCreateTime, Article::getArticleFileId).orderByAsc(Article::getCreateTime);
            IPage<Article> iPage = articleMapper.selectPage(page, queryWrapper);
            List<Article> articleList = iPage.getRecords();

            List<ArticleReviewedListVO> voList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(articleList)) {
                Set<Integer> typeSet = articleList.stream().map(Article::getArticleType).collect(Collectors.toSet());
                LambdaQueryWrapper<TechnologyType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.select(TechnologyType::getId, TechnologyType::getTypeName).in(TechnologyType::getId, typeSet);
                List<TechnologyType> technologyTypeList = technologyTypeMapper.selectList(lambdaQueryWrapper);
                Map<Integer, TechnologyType> typeMap = technologyTypeList.stream().collect(Collectors.toMap(TechnologyType::getId, Function.identity(), (o1, o2) -> o1));
                voList = articleList.stream().map(r -> {
                    ArticleReviewedListVO vo = new ArticleReviewedListVO();
                    BeanUtils.copyProperties(r, vo);
                    TechnologyType type = typeMap.get(r.getArticleType());
                    if (!ObjectUtils.isEmpty(type)) {
                        vo.setTypeName(type.getTypeName());
                    }
                    vo.setDownLoadFlag(StringUtils.hasLength(r.getArticleFileId()) ? Boolean.TRUE : Boolean.FALSE);
                    return vo;
                }).collect(Collectors.toList());
            }
            pageBean.setPageNum(iPage.getCurrent());
            pageBean.setPageSize(iPage.getSize());
            pageBean.setPages(iPage.getPages());
            pageBean.setTotal(iPage.getTotal());
            pageBean.setData(voList);
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("管理员获取待审核列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取待审核列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    @Override
    public void getMyAnyStatusArticle(Integer articleId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            Article article = articleMapper.selectById(articleId);
            if (ObjectUtils.isEmpty(article)) {
                result.setData(new JSONObject());
            } else {
                if (userByToken.getId().equals(article.getArticleBelongUserId())) {
                    if (article.getArticleStatus().equals(0)) {
                        UserSelfArticleDetailVO vo = new UserSelfArticleDetailVO();
                        BeanUtils.copyProperties(article, vo);
                        result.setData(vo);
                    }
                } else {
                    result.setData(new JSONObject());
                }
            }
        } catch (Exception e) {
            log.error("获取用户任意状态文章失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户任意状态文章接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, articleId, result);
        }
    }

    @Override
    public void downloadBeforeValid(DownloadFileDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            log.info("12345687");
            UserSubject userByToken = userService.getUserByToken(request);
            // 获取文章下载所需积分
            Integer articleId = dto.getArticleId();
            Article article = articleMapper.selectById(articleId);
            if (ObjectUtils.isEmpty(article) || !article.getArticleStatus().equals(CommonContent.ARTICLE_PASS)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章不存在");
                return;
            }
            DownloadBeforeValidVO vo = new DownloadBeforeValidVO();
            String downToken = IdUtil.simpleUUID();
            String articleFileId = article.getArticleFileId();
            if (!StringUtils.hasLength(articleFileId) || !articleFileId.equals(dto.getFileId())) {
                vo.setFlag(Boolean.FALSE);
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setData(vo);
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章或参数传递异常");
                return;
            }
            Integer articleDownloadPoint = article.getArticleDownloadPoint();
            LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Order::getArticleId, article.getArticleId()).eq(Order::getUserId, userByToken.getId());
            Integer count = orderMapper.selectCount(queryWrapper);
            if (articleDownloadPoint == 0 || count > 0 || userByToken.getId().equals(article.getArticleBelongUserId())) {
                vo.setFlag(Boolean.TRUE);
                vo.setDownToken(downToken);
                redisUtil.set(redisUtil.getCacheKey(CachePrefixContent.FILE_BEFORE_DOWNLOAD_VALID, downToken), JSON.toJSONString(dto), 1L, TimeUnit.MINUTES);
                result.setData(vo);
            } else {
                String id = userByToken.getId();
                Integer valid = articleMapper.checkDownLoadPoint(id, article.getArticleId());
                if (valid == 1) {
                    vo.setFlag(Boolean.TRUE);
                    vo.setDownToken(downToken);
                    redisUtil.set(redisUtil.getCacheKey(CachePrefixContent.FILE_BEFORE_DOWNLOAD_VALID, downToken), JSON.toJSONString(dto), 1L, TimeUnit.MINUTES);
                    result.setData(vo);
                } else {
                    vo.setFlag(Boolean.FALSE);
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setData(vo);
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户积分不足");
                }
            }
        } catch (Exception e) {
            log.info("11");
            log.error("文章文件下载前置检查失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【文章文件下载前置检查接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);

        }
    }

    @Override
    @Transactional
    public void download(String k, HttpServletRequest request, HttpServletResponse response) {

        long startTime = System.currentTimeMillis();
        BaseResult result = BaseResult.ok();
        FileInputStream in = null;
        BufferedInputStream bin = null;
        try {
            log.info("123456879");
            String dToken = redisUtil.getString(redisUtil.getCacheKey(CachePrefixContent.FILE_BEFORE_DOWNLOAD_VALID, k));

            if (!StringUtils.hasLength(dToken)) {
                log.info("1");
                result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",凭证认证失败");
                log.warn("凭证认证失败: dToken is empty");
            } else {
                log.info("12");
                UserSubject userByToken = userService.getUserByToken(request);
                log.info("User retrieved by token: {}", userByToken);
                DownloadFileDTO dto = JSON.parseObject(dToken, DownloadFileDTO.class);
                Article article = articleMapper.selectById(dto.getArticleId());
                log.info("Article retrieved: {}", article);
                if (ObjectUtils.isEmpty(article)) {
                    log.info("123");
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章不存在");
                    log.warn("文章不存在: article is empty");
                } else {
                    if (!StringUtils.hasLength(article.getArticleFileId())) {
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该文章未上传文件");
                    } else {
                        log.info("1234");
                        FilePath filePath = filePathService.getById(article.getArticleFileId());
                        if (!ObjectUtils.isEmpty(filePath)) {
                            LambdaQueryWrapper<Order> orderLambdaQueryWrapper = new LambdaQueryWrapper<>();
                            orderLambdaQueryWrapper.eq(Order::getUserId, userByToken.getId()).eq(Order::getArticleId, article.getArticleId());
                            Integer exist = orderMapper.selectCount(orderLambdaQueryWrapper);
                            String fileRealPath = filePath.getFileRealPath();
                            String fileOrignName = filePath.getFileOrignName();
                            File file = new File(fileRealPath);
                            if (exist > 0 || userByToken.getId().equals(article.getArticleBelongUserId())) {
                                response.setContentType("application/x-msdownload");
                                String encode = URLEncoder.encode(fileOrignName, "utf-8");
                                fileOrignName = new String(encode.getBytes("UTF-8"), "GBK");
                                response.addHeader("Content-Disposition", "attachment;filename=" + fileOrignName);
                                ServletOutputStream out = response.getOutputStream();
                                in = new FileInputStream(file);
                                bin = new BufferedInputStream(in);
                                byte[] bytes = new byte[1024];
                                int len;
                                while ((len = bin.read(bytes)) != -1) {
                                    out.write(bytes, 0, len);
                                    out.flush();
                                }
                                out.flush();
                                return;
                            } else {
                                log.info("12345");
                                Integer validResult = articleMapper.checkDownLoadPoint(userByToken.getId(), article.getArticleId());
                                if (validResult == 1) {
                                    if (!file.exists()) {
                                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文件不存在或已过期");
                                    } else {
                                        Integer update = userMapper.updateOperaPointById(userByToken.getId(), article.getArticleDownloadPoint(), CommonContent.POINT_DECR);
                                        if (update != 1) {
                                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                                            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户积分扣除失败");
                                        } else {
                                            // 记录订单
                                            Order order = new Order();
                                            order.setArticleId(article.getArticleId());
                                            order.setUserId(userByToken.getId());
                                            order.setTradeTime(LocalDateTime.now());
                                            orderMapper.insert(order);
                                            // 记录流水
                                            if (article.getArticleDownloadPoint() > 0) {
                                                PointTradeFlow tradeFlow = new PointTradeFlow();
                                                tradeFlow.setArticleId(article.getArticleId());
                                                tradeFlow.setUserId(userByToken.getId());
                                                tradeFlow.setPoint(article.getArticleDownloadPoint());
                                                tradeFlow.setTradeTime(LocalDateTime.now());
                                                tradeFlow.setPointType(CommonContent.POINT_DECR);
                                                tradeFlow.setTradeDesc("兑换文章资源");
                                                pointTradeFlowMapper.insert(tradeFlow);
                                            }
                                            // 给发布者奖励积分
                                            Integer rewardPoint = calculateRewardPoint(article);
                                            Integer updateReward = userMapper.updateOperaPointById(article.getArticleBelongUserId(), rewardPoint, CommonContent.POINT_INCR);
                                            if (updateReward != 1) {
                                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                                                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户积分操作失败");
                                            } else {
                                                if (rewardPoint > 0) {
                                                    PointTradeFlow tradeFlow = new PointTradeFlow();
                                                    tradeFlow.setArticleId(article.getArticleId());
                                                    tradeFlow.setUserId(article.getArticleBelongUserId());
                                                    tradeFlow.setPoint(rewardPoint);
                                                    tradeFlow.setTradeTime(LocalDateTime.now());
                                                    tradeFlow.setPointType(CommonContent.POINT_INCR);
                                                    tradeFlow.setTradeDesc("文章资源被下载");
                                                    pointTradeFlowMapper.insert(tradeFlow);
                                                }
                                                // 文件传输
                                                response.setContentType("application/x-msdownload");
                                                String encode = URLEncoder.encode(fileOrignName, "utf-8");
                                                fileOrignName = new String(encode.getBytes("UTF-8"), "GBK");
                                                response.addHeader("Content-Disposition", "attachment;filename=" + fileOrignName);
                                                ServletOutputStream out = response.getOutputStream();
                                                in = new FileInputStream(file);
                                                bin = new BufferedInputStream(in);
                                                byte[] bytes = new byte[1024];
                                                int len;
                                                while ((len = bin.read(bytes)) != -1) {
                                                    out.write(bytes, 0, len);
                                                    out.flush();
                                                }
                                                out.flush();

                                                completableFutureService.runAsyncTask(() -> {
                                                    // 发送消息
                                                    String msg = "您好,用户"+userByToken.getUsername()+"下载了您发布的文章“" + article.getArticleTitle() + "”,给您的账户加上" + rewardPoint + "积分";
                                                    SystemMessage message = new SystemMessage();
                                                    message.setReceiveUserId(article.getArticleBelongUserId());
                                                    message.setCreateTime(LocalDateTime.now());
                                                    message.setMessageContent(msg);
                                                    systemMessageService.saveSysMessage(message, true, article.getArticleBelongUserId(), BaseResult.ok());
                                                });
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.info("123456");
            try {
                log.info("1234564");
                if (bin != null) {
                    bin.close();
                }
                if (in != null) {
                    in.close();
                }
                log.error("用户下载文件失败,{}", e);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } catch (IOException ioException) {
                log.info("12345687");
                ioException.printStackTrace();
            }
        } finally {
            log.info("123456879");
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户下载文件接口】【{}ms】 \n入参:{}\n出参:{}", "查询&修改", endTime - startTime, k, result);
        }
    }

    @Override
    public void getArticleMainList(GetArticleListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            BasePageBean<List<UserArticleListVO>> pageBean = null;
            // 从接口获取数据
            pageBean = getIndexArticlePageBeanFromHttp(dto);
            readStatusHandle(userByToken, pageBean);
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("用户首页获取文章列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户首页获取文章列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    /**
     * 计算积分
     * @param article
     * @return
     */
    private Integer calculateRewardPoint(Article article) {
        Integer result = 0;
        Integer point = article.getArticleDownloadPoint();
        if (point == 1 || point == 0) {
            result = point;
        } else if (point > 1 && point <= 10) {
            result = point / 2 + 1;
        } else if (point > 10 && point <= 20) {
            result = point / 2 + 3;
        } else if (point > 20 && point <= 30) {
            result = point /2 + 4;
        } else if (point > 30 && point <= 50) {
            result = point / 2 + 5;
        } else {
            result = point / 2 + 10;
        }
        return result;
    }

    @Override
    public void getArticleIndexList(GetArticleListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            BasePageBean pageBean = null;
            if (dto.getPageNum().equals(1)) {
                // 用户获取首页第一页数据、不带任何搜索条件，优先取缓存
                String value = redisUtil.getString(CachePrefixContent.INDEX_ARTICLE_LIST);
                if (StringUtils.hasLength(value)) {
                    pageBean = JSON.parseObject(value, BasePageBean.class);
                    // 处理点赞数、点赞状态
                    readStatusHandle(userByToken, pageBean);
                } else {
                    // 从接口获取数据
                    pageBean = getPageBeanFromHttp(dto);
                    redisUtil.set(CachePrefixContent.INDEX_ARTICLE_LIST, JSON.toJSONString(pageBean), commonConfig.getCacheShortTime(), TimeUnit.MINUTES);
                    readStatusHandle(userByToken, pageBean);
                }
            } else {
                // 从接口获取数据
                pageBean = getPageBeanFromHttp(dto);
                readStatusHandle(userByToken, pageBean);
            }
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("用户获取首页文章列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户获取首页文章列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    @Override
    public void adminGetArticleInfo(Integer articleId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Article article = articleMapper.selectById(articleId);
            if (ObjectUtils.isEmpty(article)) {
                result.setData(null);
            } else {
                AdminArticleInfoVO vo = new AdminArticleInfoVO();
                BeanUtils.copyProperties(article, vo);
                TechnologyType technologyType = technologyTypeMapper.selectById(article.getArticleType());
                TechnologyTypeVO typeVO = new TechnologyTypeVO();
                BeanUtils.copyProperties(technologyType, typeVO);
                vo.setType(typeVO);
                // 标签
                List<Tag> tags = tagMapper.selectTagListByArticleId(articleId);
                vo.setTags(CollectionUtils.isEmpty(tags) ? new ArrayList<>(0) : tags.stream().map(t -> {
                    TagVO tagVO = new TagVO();
                    BeanUtils.copyProperties(t, tagVO);
                    return tagVO;
                }).collect(Collectors.toList()));
                if (StringUtils.hasLength(article.getArticleFileId())) {
                    FilePath filePath = filePathService.getById(article.getArticleFileId());
                    if (!ObjectUtils.isEmpty(filePath)) {
                        FileVO fileVO = new FileVO();
                        BeanUtils.copyProperties(filePath, fileVO);
                        fileVO.setFileName(filePath.getFileOrignName());
                        fileVO.setFileStructure(JSON.parseArray(filePath.getFileStructure()));
                        vo.setFileInfo(fileVO);
                    }
                }
                result.setData(vo);
            }
        } catch (Exception e) {
            log.error("管理员获取文章详情失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取文章详情接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, articleId, result);
        }
    }

    @Override
    public void reviewedArticle(ReviewedArticleDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Article article = articleMapper.selectById(dto.getArticleId());
            if (ObjectUtils.isEmpty(article)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章不存在");
            } else {
                Article temp = new Article();
                BeanUtils.copyProperties(dto, temp);
                int update = articleMapper.updateById(temp);
                if (update != 1) {
                    log.error(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章审核失败");
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章审核失败");
                } else {
                    Integer integer = userMapper.updateOperaPointById(article.getArticleBelongUserId(), 1, CommonContent.POINT_INCR);
                    if (integer == 1) {
                        PointTradeFlow tradeFlow = new PointTradeFlow();
                        tradeFlow.setArticleId(article.getArticleId());
                        tradeFlow.setUserId(article.getArticleBelongUserId());
                        tradeFlow.setPoint(1);
                        tradeFlow.setTradeTime(LocalDateTime.now());
                        tradeFlow.setPointType(CommonContent.POINT_INCR);
                        tradeFlow.setTradeDesc("文章审核通过");
                        pointTradeFlowMapper.insert(tradeFlow);

                        completableFutureService.runAsyncTask(() -> {
                            Integer articleStatus = dto.getArticleStatus();
                            String msg = null;
                            if (articleStatus == 1) {
                                msg = "您好,您发布的文章“" + article.getArticleTitle() + "”审核通过,给您的账户加上" + 1 + "积分";
                                Integer articleDownloadPoint = dto.getArticleDownloadPoint();
                                if (null != articleDownloadPoint) {
                                    msg = msg + ",文章设置下载积分为" + articleDownloadPoint;
                                }
                            } else if (articleStatus == 2) {
                                msg = "您好,您发布的文章“" + article.getArticleTitle() + "”审核未通过," + dto.getArticleDesc();
                            }
                            // 发送消息
                            SystemMessage message = new SystemMessage();
                            message.setReceiveUserId(article.getArticleBelongUserId());
                            message.setCreateTime(LocalDateTime.now());
                            message.setMessageContent(msg);
                            systemMessageService.saveSysMessage(message, true, article.getArticleBelongUserId(), BaseResult.ok());
                        });
                    }
                }
            }
        } catch (Exception e) {
            log.error("管理员审核文章失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员审核文章接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void adminDownload(String fileId, HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        BaseResult result = BaseResult.ok();
        FileInputStream in = null;
        BufferedInputStream bin = null;
        try {
            FilePath filePath = filePathService.getById(fileId);
            if (!ObjectUtils.isEmpty(filePath)) {
                String fileRealPath = filePath.getFileRealPath();
                String fileOrignName = filePath.getFileOrignName();
                File file = new File(fileRealPath);
                if (null != file) {
                    response.setContentType("application/x-msdownload");
                    String encode = URLEncoder.encode(fileOrignName, "utf-8");
                    fileOrignName = new String(encode.getBytes("UTF-8"), "GBK");
                    response.addHeader("Content-Disposition", "attachment;filename=" + fileOrignName);
                    ServletOutputStream out = response.getOutputStream();
                    in = new FileInputStream(file);
                    bin = new BufferedInputStream(in);
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = bin.read(bytes)) != -1) {
                        out.write(bytes, 0, len);
                        out.flush();
                    }
                    out.flush();
                }
            }
        } catch (Exception e) {
            try {
                if (bin != null) {
                    bin.close();
                }
                if (in != null) {
                    in.close();
                }
                log.error("下载文件失败,{}", e);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【下载文件接口】【{}ms】 \n入参:{}\n出参:{}", "查询&修改", endTime - startTime, fileId, result);
        }
    }

    @Override
    @Transactional
    public void adminUpdateArticle(AdminUpdateArticleDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Article article = articleMapper.selectById(dto.getArticleId());
            if (ObjectUtils.isEmpty(article)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章不存在");
            } else {
                Article updateArticle = new Article();
                BeanUtils.copyProperties(dto, updateArticle);
                int update = articleMapper.updateById(updateArticle);
                if (update != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",文章修改失败");
                }
            }
        } catch (Exception e) {
            log.error("管理员修改文章失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员修改文章接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void adminDeleteArticle(Integer articleId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Article article = articleMapper.selectById(articleId);
            if (ObjectUtils.isEmpty(article)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",文章不存在");
            } else {
                int update = articleMapper.deleteById(articleId);
                if (update != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",文章删除失败");
                }
            }
        } catch (Exception e) {
            log.error("管理员文章删除失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员删除文章接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, articleId, result);
        }
    }

    @Override
    public void adminGetArticleList(AdminGetArticleListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            BasePageBean pageBean = new BasePageBean();
            Page<Article> page = new Page<>(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasLength(dto.getKeyWord())) {
                queryWrapper.like(Article::getArticleTitle, dto.getKeyWord());
            }
            if (!ObjectUtils.isEmpty(dto.getArticleType())) {
                queryWrapper.eq(Article::getArticleType, dto.getArticleType());
            }
            if (!ObjectUtils.isEmpty(dto.getArticleIsSelf())) {
                queryWrapper.eq(Article::getArticleIsSelf, dto.getArticleIsSelf());
            }
            if (!ObjectUtils.isEmpty(dto.getArticleStatus())) {
                queryWrapper.eq(Article::getArticleStatus, dto.getArticleStatus());
            }
            queryWrapper.select(Article::getArticleId, Article::getArticleTitle, Article::getArticleIsSelf, Article::getArticleType, Article::getArticleStatus, Article::getCreateTime, Article::getArticleFileId).orderByAsc(Article::getCreateTime);
            IPage<Article> iPage = articleMapper.selectPage(page, queryWrapper);
            List<Article> articleList = iPage.getRecords();

            List<ArticleReviewedListVO> voList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(articleList)) {
                Set<Integer> typeSet = articleList.stream().map(Article::getArticleType).collect(Collectors.toSet());
                LambdaQueryWrapper<TechnologyType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.select(TechnologyType::getId, TechnologyType::getTypeName).in(TechnologyType::getId, typeSet);
                List<TechnologyType> technologyTypeList = technologyTypeMapper.selectList(lambdaQueryWrapper);
                Map<Integer, TechnologyType> typeMap = technologyTypeList.stream().collect(Collectors.toMap(TechnologyType::getId, Function.identity(), (o1, o2) -> o1));
                voList = articleList.stream().map(r -> {
                    ArticleReviewedListVO vo = new ArticleReviewedListVO();
                    BeanUtils.copyProperties(r, vo);
                    TechnologyType type = typeMap.get(r.getArticleType());
                    if (!ObjectUtils.isEmpty(type)) {
                        vo.setTypeName(type.getTypeName());
                    }
                    vo.setDownLoadFlag(StringUtils.hasLength(r.getArticleFileId()) ? Boolean.TRUE : Boolean.FALSE);
                    return vo;
                }).collect(Collectors.toList());
            }
            pageBean.setPageNum(iPage.getCurrent());
            pageBean.setPageSize(iPage.getSize());
            pageBean.setPages(iPage.getPages());
            pageBean.setTotal(iPage.getTotal());
            pageBean.setData(voList);
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("管理员获取文章列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取文章列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    @Override
    public void getUpdateArticleInfo(Integer articleId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            Article article = articleMapper.selectById(articleId);
            if (ObjectUtils.isEmpty(article)) {
                result.setData(null);
            } else {
                UserUpdateArticleInfoVO vo = new UserUpdateArticleInfoVO();
                BeanUtils.copyProperties(article, vo);
                TechnologyType technologyType = technologyTypeMapper.selectById(article.getArticleType());
                TechnologyTypeVO typeVO = new TechnologyTypeVO();
                BeanUtils.copyProperties(technologyType, typeVO);
                vo.setType(typeVO);
                // 标签
                List<Tag> tags = tagMapper.selectTagListByArticleId(articleId);
                vo.setTags(CollectionUtils.isEmpty(tags) ? new ArrayList<>(0) : tags.stream().map(t -> {
                    TagVO tagVO = new TagVO();
                    BeanUtils.copyProperties(t, tagVO);
                    return tagVO;
                }).collect(Collectors.toList()));

                if (StringUtils.hasLength(article.getArticleFileId())) {
                    FilePath filePath = filePathService.getById(article.getArticleFileId());
                    if (!ObjectUtils.isEmpty(filePath)) {
                        FileVO fileVO = new FileVO();
                        BeanUtils.copyProperties(filePath, fileVO);
                        fileVO.setFileName(filePath.getFileOrignName());
                        fileVO.setFileStructure(JSON.parseArray(filePath.getFileStructure()));
                        vo.setFileInfo(fileVO);
                    }
                }
                result.setData(vo);
            }
        } catch (Exception e) {
            log.error("用户修改文章获取文章详情失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户修改文章获取文章详情接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, articleId, result);
        }
    }

    @Override
    public void getArticleFileInfo(Integer articleId, HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        FileInputStream in = null;
        BufferedInputStream bin = null;
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            Article article = articleMapper.selectById(articleId);
            if (!ObjectUtils.isEmpty(article) && userByToken.getId().equals(article.getArticleBelongUserId()) && StringUtils.hasLength(article.getArticleFileId())) {
                FilePath filePath = filePathService.getById(article.getArticleFileId());
                if (!ObjectUtils.isEmpty(filePath)) {
                    String fileRealPath = filePath.getFileRealPath();
                    String fileOrignName = filePath.getFileOrignName();
                    File file = new File(fileRealPath);
                    if (null != file) {
                        response.setContentType("application/x-msdownload");
                        String encode = URLEncoder.encode(fileOrignName, "utf-8");
                        fileOrignName = new String(encode.getBytes("UTF-8"), "GBK");
                        response.addHeader("Content-Disposition", "attachment;filename=" + fileOrignName);
                        ServletOutputStream out = response.getOutputStream();
                        in = new FileInputStream(file);
                        bin = new BufferedInputStream(in);
                        byte[] bytes = new byte[1024];
                        int len;
                        while ((len = bin.read(bytes)) != -1) {
                            out.write(bytes, 0, len);
                            out.flush();
                        }
                        out.flush();
                    }
                }
            }
        } catch (Exception e) {
            try {
                if (bin != null) {
                    bin.close();
                }
                if (in != null) {
                    in.close();
                }
                log.error("用户获取文件失败,{}", e);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户获取文件接口】【{}ms】 \n入参:{}\n出参:{}", "查询&修改", endTime - startTime, articleId, null);
        }
    }
}




