package com.cxs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.dto.article.SaveOrUpdateArticleDTO;
import com.cxs.dto.article.SaveOrUpdateArticleDraftDTO;
import com.cxs.dto.tag.TagDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.model.ArticleDraft;
import com.cxs.model.Tag;
import com.cxs.service.ArticleDraftService;
import com.cxs.mapper.ArticleDraftMapper;
import com.cxs.service.UserService;
import com.cxs.vo.article.ArticleDraftVO;
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
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ArticleDraftServiceImpl extends ServiceImpl<ArticleDraftMapper, ArticleDraft> implements ArticleDraftService{


    @Autowired
    private UserService userService;

    @Autowired
    private ArticleDraftMapper articleDraftMapper;

    @Override
    public void getArticleDraft(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            if (ObjectUtils.isEmpty(userByToken)) {
                result.setData(new JSONObject());
            } else {
                ArticleDraft articleDraft = articleDraftMapper.selectById(userByToken.getId());
                if (ObjectUtils.isEmpty(articleDraft)) {
                    result.setData(new JSONObject());
                } else {
                    ArticleDraftVO vo = new ArticleDraftVO();
                    BeanUtils.copyProperties(articleDraft, vo);
                    vo.setTags(StringUtils.hasLength(articleDraft.getArticleTags()) ? Arrays.stream(articleDraft.getArticleTags().split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()) : new ArrayList<>(0));
                    result.setData(vo);
                }
            }
        } catch (Exception e) {
            log.error("用户获取草稿失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户获取草稿接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void saveOrUpdateDraft(SaveOrUpdateArticleDraftDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            if (ObjectUtils.isEmpty(userByToken)) {
                result.setData(new JSONObject());
            } else {
                ArticleDraft articleDraft = articleDraftMapper.selectById(userByToken.getId());
                ArticleDraft updateDraft = new ArticleDraft();
                BeanUtils.copyProperties(dto, updateDraft);
                updateDraft.setUserId(userByToken.getId());
                updateDraft.setArticleTags(CollectionUtils.isEmpty(dto.getTags()) ? null : dto.getTags().stream().map(t -> t.getTagId().toString()).collect(Collectors.joining(",")));
                if (ObjectUtils.isEmpty(articleDraft)) {
                    updateDraft.setCreateTime(LocalDateTime.now());
                    int insert = articleDraftMapper.insert(updateDraft);
                    if (insert != 1) {
                        result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                    }
                } else {
                    updateDraft.setUpdateTime(LocalDateTime.now());
                    int update = articleDraftMapper.updateById(updateDraft);
                    if (update != 1) {
                        result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                    }
                }
            }
        } catch (Exception e) {
            log.error("用户修改草稿失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户修改草稿接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, "", result);
        }
    }

    @Override
    public void deleteDraft(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = userService.getUserByToken(request);
            if (ObjectUtils.isEmpty(userByToken)) {
                result.setData(new JSONObject());
            } else {
                articleDraftMapper.deleteById(userByToken.getId());
            }
        } catch (Exception e) {
            log.error("用户删除草稿失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户删除草稿接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, "", result);
        }
    }
}




