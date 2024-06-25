package com.hitchater.chatapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitchater.chatapp.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM t_user WHERE user_name = #{username} AND password = #{password}")
    User selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Results({
            @Result(property = "user_id", column = "user_id"),
            @Result(property = "user_name", column = "user_name"),
            @Result(property = "password", column = "password"),
            @Result(property = "avatar", column = "avatar")
    })
    @Select("SELECT user_id, user_name, password, avatar FROM t_user WHERE user_name = #{username}")
    User selectByUsername(@Param("username") String username);






    @Select("SELECT user_id, user_name, password, avatar FROM t_user WHERE user_name LIKE CONCAT('%', #{user_name}, '%')")
    @Results({
            @Result(property = "user_id", column = "user_id"),
            @Result(property = "user_name", column = "user_name"),
            @Result(property = "password", column = "password"),
            @Result(property = "avatar", column = "avatar")
    })
    List<User> searchByUsername(@Param("user_name") String user_name);

    @Select("SELECT user_id, user_name, password, avatar FROM t_user WHERE user_id = #{user_id}")
    @Results({
            @Result(property = "user_id", column = "user_id"),
            @Result(property = "user_name", column = "user_name"),
            @Result(property = "password", column = "password"),
            @Result(property = "avatar", column = "avatar")
    })
    User selectById(@Param("user_id") String user_id);

    @Select("<script>" +
            "SELECT * FROM t_user WHERE user_id IN " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<User> selectBatchIds(@Param("ids") List<Integer> ids);
}
