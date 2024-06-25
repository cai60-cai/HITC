package com.cxs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxs.base.BasePageBean;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.bo.UserArticleListBo;
import com.cxs.config.CommonConfig;
import com.cxs.constant.CachePrefixContent;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.mapper.ArticleMapper;
import com.cxs.mapper.SystemInfoMapper;
import com.cxs.mapper.TagMapper;
import com.cxs.mapper.TechnologyTypeMapper;
import com.cxs.mapper.UserMapper;
import com.cxs.model.Article;
import com.cxs.model.ArticleRead;
import com.cxs.model.SystemInfo;
import com.cxs.model.TechnologyType;
import com.cxs.service.ArticleService;
import com.cxs.service.SystemService;
import com.cxs.utils.RedisUtil;
import com.cxs.utils.TimeUtil;
import com.cxs.vo.article.UserArticleListVO;
import com.cxs.vo.main.AboutSysInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TechnologyTypeMapper technologyTypeMapper;

    @Autowired
    private SystemInfoMapper systemInfoMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CommonConfig commonConfig;

    @Override
    public void getAboutSysInfo(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            SystemInfo systemInfo = null;
            AboutSysInfoVO vo = new AboutSysInfoVO();
            Object value = redisUtil.getValue(CachePrefixContent.INDEX_SYS_INFO);
            if (!ObjectUtils.isEmpty(value)) {
                systemInfo = JSON.parseObject(JSON.toJSONString(value), SystemInfo.class);
            } else {
                systemInfo = systemInfoMapper.selectOne(new QueryWrapper<>());
                redisUtil.set(CachePrefixContent.INDEX_SYS_INFO, systemInfo, commonConfig.getCacheLongTime(), TimeUnit.MINUTES);
            }
            if (ObjectUtils.isEmpty(systemInfo)) {
                return;
            }
            BeanUtils.copyProperties(systemInfo, vo);
            // 文章总数
            QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
            articleQueryWrapper.eq("article_status", 1);
            vo.setArticleCount(articleMapper.selectCount(articleQueryWrapper));
            articleQueryWrapper.eq("article_is_self", 1);
            vo.setOriginArticleCount(articleMapper.selectCount(articleQueryWrapper));
            vo.setUserCount(userMapper.selectCount(new QueryWrapper<>()));
            vo.setTagCount(tagMapper.selectCount(new QueryWrapper<>()));

            List<TechnologyType> technologyTypes = technologyTypeMapper.selectList(new QueryWrapper<>());
            List<TechnologyType> secondList = CollectionUtils.isEmpty(technologyTypes) ? new ArrayList<>(0) :
                    technologyTypes.stream().filter(technologyType -> !technologyType.getTypeParentId().equals(-1)).collect(Collectors.toList());
            vo.setTypeCount(secondList.size());
            result.setData(vo);
        } catch (Exception e) {
            log.error("获取关于本站信息失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取关于本站信息接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void getSystemInfo(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            SystemInfo systemInfo = null;
            Object value = redisUtil.getValue(CachePrefixContent.INDEX_SYS_INFO);
            if (!ObjectUtils.isEmpty(value)) {
                systemInfo = JSON.parseObject(JSON.toJSONString(value), SystemInfo.class);
            } else {
                systemInfo = systemInfoMapper.selectOne(new QueryWrapper<>());
                redisUtil.set(CachePrefixContent.INDEX_SYS_INFO, systemInfo, commonConfig.getCacheLongTime(), TimeUnit.MINUTES);
            }
            if (ObjectUtils.isEmpty(systemInfo)) {
                return;
            }
            result.setData(systemInfo);
        } catch (Exception e) {
            log.error("获取关于本站信息失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取关于本站信息接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }
}
