package com.hitchater.chatapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitchater.chatapp.entity.Friend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FriendMapper extends BaseMapper<Friend> {

    @Select("SELECT f.id, f.user_id, f.friend_id, u.user_name AS friend_user_name " +
            "FROM friend f " +
            "JOIN t_user u ON f.friend_id = u.user_id " +
            "WHERE f.user_id = #{userId}")
    List<Friend> selectFriendsByUserId(String userId);
    @Select("SELECT * FROM friend WHERE (user_id = #{userId} AND friend_id = #{friendId}) OR (user_id = #{friendId} AND friend_id = #{userId})")
    Friend selectFriendRelation(@Param("userId") String userId, @Param("friendId") String friendId);


    @Select("SELECT f.id, f.user_id, f.friend_id, u.user_name " +
            "FROM friend f " +
            "JOIN t_user u ON f.friend_id = u.user_id " +
            "WHERE f.user_id = #{user_id}")
    List<Friend> getFriendsWithUsername(@Param("user_id") String user_id);
}
