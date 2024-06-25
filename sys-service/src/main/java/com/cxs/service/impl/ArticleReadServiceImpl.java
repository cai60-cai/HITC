package com.cxs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.model.ArticleRead;
import com.cxs.service.ArticleReadService;
import com.cxs.mapper.ArticleReadMapper;
import org.springframework.stereotype.Service;

@Service
public class ArticleReadServiceImpl extends ServiceImpl<ArticleReadMapper, ArticleRead>
    implements ArticleReadService{

}




