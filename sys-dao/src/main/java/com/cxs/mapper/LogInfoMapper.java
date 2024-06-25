package com.cxs.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxs.bo.LogInfoBo;
import com.cxs.dto.admin.log.GetLogListDTO;
import com.cxs.model.LogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


public interface LogInfoMapper extends BaseMapper<LogInfo> {

    /**
     * query查询日志
     * @param page
     * @param dto
     * @return
     */
    Page<LogInfoBo> selectListByQuery(@Param("page") IPage<LogInfoBo> page, @Param("dto") GetLogListDTO dto);
}




