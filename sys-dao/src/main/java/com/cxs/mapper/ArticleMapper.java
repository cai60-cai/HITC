package com.cxs.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cxs.bo.ArticleRangeCountBo;
import com.cxs.bo.SystemRecommendArticleBO;
import com.cxs.bo.UserArticleListBo;
import com.cxs.model.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxs.vo.article.SystemRecommendArticleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ArticleMapper extends BaseMapper<Article> {

    IPage<UserArticleListBo> selectArticleList(IPage page,
                                               @Param("keyword") String keyword,
                                               @Param("tags") List<Integer> tags,
                                               @Param("typeId") Integer typeId,
                                               @Param("tagId") Integer tagId,
                                               @Param("flag") Integer flag);

    List<SystemRecommendArticleBO> selectSystemRecommend(IPage<Article> page);

    Integer checkDownLoadPoint(@Param("id") String id, @Param("articleId") Integer articleId);

    List<ArticleRangeCountBo> selectRangeCreateTimeCountInfo(@Param("num") Integer num);
}




