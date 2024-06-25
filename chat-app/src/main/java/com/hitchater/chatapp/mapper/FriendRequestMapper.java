package com.hitchater.chatapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitchater.chatapp.entity.FriendRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FriendRequestMapper extends BaseMapper<FriendRequest> {
    @Select("SELECT id, sender_id, receiver_id, accepted, sender_username FROM friend_request WHERE receiver_id = #{userId} AND accepted = FALSE")
    List<FriendRequest> getFriendRequestsWithUsername(@Param("userId") String userId);

    @Select("SELECT id, sender_id, receiver_id, accepted, sender_username FROM friend_request WHERE sender_id = #{senderId} AND receiver_id = #{receiverId}")
    FriendRequest selectBySenderAndReceiver(@Param("senderId") String senderId, @Param("receiverId") String receiverId);
}
