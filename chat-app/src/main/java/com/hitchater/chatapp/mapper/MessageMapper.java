package com.hitchater.chatapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hitchater.chatapp.entity.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
