package com.cxs.mapper;

import com.cxs.model.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author cxs
* @description 针对表【t_tag(标签表)】的数据库操作Mapper
* @createDate 2022-11-16 14:38:35
* @Entity com.cxs.model.Tag
*/
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id获取tags
     * @param articleId
     * @return
     */
    List<Tag> selectTagListByArticleId(@Param("articleId") Integer articleId);
}




