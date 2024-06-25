package com.cxs.mapper;

import com.cxs.model.NavLink;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author cxs
* @description 针对表【t_nav_link(nav-top链接表)】的数据库操作Mapper
* @createDate 2022-11-16 10:14:19
* @Entity com.cxs.model.NavLink
*/
public interface NavLinkMapper extends BaseMapper<NavLink> {
    /**
     * 获取oder最大的一个
     * @return
     */
    NavLink selectMaxOrderNavLink();

    /**
     * 根据order获取navLink
     * @param minNavOrder
     * @param maxNavOrder
     * @return
     */
    List<NavLink> selectNavLinkByOrderRange(@Param("minNavOrder") Integer minNavOrder, @Param("maxNavOrder") Integer maxNavOrder);

    /**
     * 批量修改order
     * @param targetNavLinkList
     * @return
     */
    Integer updateByPrimaryKeyBatchs(@Param("list") List<NavLink> targetNavLinkList);

    /**
     * 根据顺序查询
     * @param order
     * @return
     */
    NavLink selectNavLinkByOrder(@Param("order") Integer order);

    /**
     * 获取该order之后的数据
     * @param order
     * @return
     */
    List<NavLink> selectNavLinkAfterOrder(@Param("order") Integer order);
}




