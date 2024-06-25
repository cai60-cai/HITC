package com.cxs.mapper;

import com.cxs.dto.profile.UserupdateRewardImgInfoDTO;
import com.cxs.model.UserReward;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author DELL
* @description 针对表【t_user_reward(用户打赏配置表)】的数据库操作Mapper
* @createDate 2022-12-03 16:53:01
* @Entity com.cxs.model.UserReward
*/
public interface UserRewardMapper extends BaseMapper<UserReward> {

    int updateIfExistInsert(UserReward reward);
}




