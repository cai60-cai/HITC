package com.cxs.mapper;

import com.cxs.model.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author DELL
* @description 针对表【t_role(角色表)】的数据库操作Mapper
* @createDate 2022-11-12 12:37:56
* @Entity com.cxs.model.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据userId关联查询用户角色
     * @param userId
     * @return
     */
    List<Role> selectRoleByUserId(@Param("userId") String userId);
}




