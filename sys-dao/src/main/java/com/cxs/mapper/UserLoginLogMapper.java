package com.cxs.mapper;

import com.cxs.bo.UserLoginRangeCountBo;
import com.cxs.model.UserLoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
* @author DELL
* @description 针对表【t_user_login_log(用户登录流水表)】的数据库操作Mapper
* @createDate 2022-11-12 12:37:56
* @Entity com.cxs.model.UserLoginLog
*/
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog> {

    List<UserLoginRangeCountBo> selectRangeCreateTimeCountInfo(@Param("num") Integer num);

    Integer selectTodayLoginCount(@Param("now") LocalDateTime now);
}




