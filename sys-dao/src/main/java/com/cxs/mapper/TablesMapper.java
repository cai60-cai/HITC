package com.cxs.mapper;

import com.cxs.bo.TableInfoBo;
import com.cxs.model.Tables;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author cxs
* @description 针对表【TABLES】的数据库操作Mapper
* @createDate 2022-11-15 16:01:56
* @Entity com.cxs.model.Tables
*/
public interface TablesMapper extends BaseMapper<Tables> {
    List<TableInfoBo> selectTables(@Param("database") String database, @Param("keyWord") String keyWord);
}




