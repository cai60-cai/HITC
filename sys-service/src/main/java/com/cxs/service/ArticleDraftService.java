package com.cxs.service;

import com.cxs.base.BaseResult;
import com.cxs.dto.article.SaveOrUpdateArticleDTO;
import com.cxs.dto.article.SaveOrUpdateArticleDraftDTO;
import com.cxs.model.ArticleDraft;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;


public interface ArticleDraftService extends IService<ArticleDraft> {

    /**
     * 获取草稿
     * @param request
     * @param result
     */
    void getArticleDraft(HttpServletRequest request, BaseResult result);

    /**
     * 保存、修改草稿
     * @param dto
     * @param request
     * @param result
     */
    void saveOrUpdateDraft(SaveOrUpdateArticleDraftDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 用户删除草稿
     * @param request
     * @param result
     */
    void deleteDraft(HttpServletRequest request, BaseResult result);

}
