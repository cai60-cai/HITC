package com.cxs.mapper;

import com.cxs.bo.UserRangeCountBo;
import com.cxs.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author DELL
* @description 针对表【t_user(用户信息表)】的数据库操作Mapper
* @createDate 2022-11-12 12:37:56
* @Entity com.cxs.model.User
*/
public interface UserMapper extends BaseMapper<User> {

    /**
     * @param id 用户id
     * @param point 积分值
     * @param type 1 + 0 -
     * @return
     */
    Integer updateOperaPointById(@Param("id") String id, @Param("point") Integer point, @Param("type") Integer type);

    /**
     * 查询num条用户注册统计数量
     * @param num
     * @return
     */
    List<UserRangeCountBo> selectRangeCreateTimeCountInfo(@Param("num") Integer num);
}




