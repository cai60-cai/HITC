package com.cxs.runner.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cxs.base.BaseResult;
import com.cxs.bo.ArticleHotSearchBo;
import com.cxs.config.CommonConfig;
import com.cxs.constant.CachePrefixContent;
import com.cxs.mapper.ArticleSearchFlowMapper;
import com.cxs.model.ArticleSearchFlow;
import com.cxs.runner.AbstractScheduledTaskJob;
import com.cxs.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

  
@Slf4j
@Service
public class HotSearchArticleTask extends AbstractScheduledTaskJob implements BeanNameAware {

    @Autowired
    private ArticleSearchFlowMapper articleSearchFlowMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CommonConfig commonConfig;


    @Override
    public void execute(BaseResult result) {
        String msg = "";
        // 清理3天前的搜索数据
        LocalDateTime localDateTime = LocalDateTime.now();
        LambdaQueryWrapper<ArticleSearchFlow> wrapper = new LambdaQueryWrapper<>();
        wrapper.le(ArticleSearchFlow::getSearchTime, localDateTime.minusDays(commonConfig.getArticleHotSearchDataSpace()));
        int delete = articleSearchFlowMapper.delete(wrapper);
        // 获取10篇搜索次数最多的文章
        List<ArticleHotSearchBo> articleHotSearchBos = articleSearchFlowMapper.selectSearchCountList(commonConfig.getArticleHotSearchlistNum());
        // 存redis，24小时
        redisUtil.set(CachePrefixContent.INDEX_ARTICLE_HOT_LIST, articleHotSearchBos, 24L, TimeUnit.HOURS);
        msg = "清理3天前的搜索数据,清理条数:" + delete;
        result.setMsg(msg);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
