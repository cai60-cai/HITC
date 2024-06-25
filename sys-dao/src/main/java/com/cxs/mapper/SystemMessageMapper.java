package com.cxs.mapper;

import com.cxs.model.SystemMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
* @author DELL
* @description 针对表【t_system_message(系统通知表)】的数据库操作Mapper
* @createDate 2024-06-10 15:54:24
* @Entity com.cxs.model.SystemMessage
*/
public interface SystemMessageMapper extends BaseMapper<SystemMessage> {

    /**
     * 修改消息状态
     * @param id
     * @param messageIdList
     * @return
     */
    int updateReadFlagByTargetIdAndIds(@Param("targetId") String id, @Param("ids") Set<Integer> messageIdList);

}




