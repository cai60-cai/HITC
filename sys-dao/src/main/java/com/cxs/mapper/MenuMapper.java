package com.cxs.mapper;

import com.cxs.model.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据userId获取所有menuId
     * @param userId
     * @return
     */
    List<Integer> getAllMenuIdByUserId(@Param("userId") String userId);
}




