package com.cxs.mapper;

import com.cxs.bo.ArticleHotSearchBo;
import com.cxs.model.ArticleSearchFlow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ArticleSearchFlowMapper extends BaseMapper<ArticleSearchFlow> {
    /**
     * 获取搜索次数最多的num条
     * @param num
     * @return
     */
    List<ArticleHotSearchBo> selectSearchCountList(@Param("num") Integer num);
}




