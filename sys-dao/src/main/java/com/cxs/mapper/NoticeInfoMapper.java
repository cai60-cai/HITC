package com.cxs.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxs.bo.NoticeInfoBo;
import com.cxs.dto.admin.notice.GetNoticeListDTO;
import com.cxs.model.NoticeInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author DELL
* @description 针对表【t_notice_info(公告信息表)】的数据库操作Mapper
* @createDate 2022-12-06 16:08:25
* @Entity com.cxs.model.NoticeInfo
*/
public interface NoticeInfoMapper extends BaseMapper<NoticeInfo> {
    /**
     * query查询公告
     * @param page
     * @param dto
     * @return
     */
    Page<NoticeInfoBo> selectListByQuery(@Param("page") IPage<NoticeInfoBo> page, @Param("dto") GetNoticeListDTO dto);
}




