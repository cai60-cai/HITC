package com.cxs.service;

import com.cxs.base.BaseRequest;
import com.cxs.base.BaseResult;
import com.cxs.base.UserSubject;
import com.cxs.dto.KeyWordSearchDTO;
import com.cxs.dto.UserLoginDTO;
import com.cxs.dto.UserRegirsterDTO;
import com.cxs.dto.admin.user.CreateUserDTO;
import com.cxs.dto.admin.user.GetSimpleUserListDTO;
import com.cxs.dto.admin.user.GetUserListDTO;
import com.cxs.dto.admin.user.UpdateStatusDTO;
import com.cxs.dto.admin.user.UpdateUserAuthDTO;
import com.cxs.dto.admin.user.UpdateUserInfoDTO;
import com.cxs.dto.admin.user.UpdateUserPwdDTO;
import com.cxs.dto.profile.CheckEmailBindDTO;
import com.cxs.dto.profile.CheckOldPasswordDTO;
import com.cxs.dto.profile.CheckUsernameDTO;
import com.cxs.dto.profile.UpdatePwdDTO;
import com.cxs.dto.profile.UpdateSelfInfoDTO;
import com.cxs.dto.profile.UserPointSettingDTO;
import com.cxs.dto.profile.UserReceiveEmailNoticeSettingDTO;
import com.cxs.dto.profile.UserRewardSettingDTO;
import com.cxs.dto.profile.UserupdateRewardImgInfoDTO;
import com.cxs.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService extends IService<User> {

    /**
     * 用户认证
     * @param dto
     * @param request
     * @param result
     */
    void login(UserLoginDTO dto, HttpServletRequest request, HttpServletResponse response, BaseResult result);

    /**
     * 验证token
     * @param request
     * @param result
     */
    void checkToken(HttpServletRequest request, BaseResult result);

    /**
     * 验证token
     * @param token
     * @param result
     */
    void checkToken(String token, BaseResult result);

    /**
     * 退出登录
     * @param request
     * @param result
     */
    void logout(HttpServletRequest request, HttpServletResponse response, BaseResult result);

    /**
     * 管理员登录
     * @param dto
     * @param request
     * @param result
     */
    void adminLogin(UserLoginDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取用户登陆日志
     * @param dto
     * @param result
     */
    void getUserLoginLog(BaseRequest dto, HttpServletRequest request, BaseResult result);


    /**
     * 根据token获取用户
     * @param request
     * @return
     */
    UserSubject getUserByToken(HttpServletRequest request);

    /**
     * 根据token获取用户
     * @param token
     * @return
     */
    UserSubject getUserByToken(String token);

    /**
     * 获取用户信息
     * @param request
     * @param result
     */
    void getUserInfo(HttpServletRequest request, BaseResult result);

    /**
     * 关键字 + 分页获取用户列表
     * @param dto
     * @param request
     * @param result
     */
    void getUserList(GetUserListDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 修改用户状态
     * @param dto
     * @param request
     * @param result
     */
    void updateStatus(UpdateStatusDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 重置用户密码
     * @param userId
     * @param request
     * @param result
     */
    void resetUserPwd(String userId, HttpServletRequest request, BaseResult result);

    /**
     * 删除用户
     * @param userId
     * @param request
     * @param result
     */
    void deleteUser(String userId, HttpServletRequest request, BaseResult result);

    /**
     * 创建用户
     * @param dto
     * @param request
     * @param result
     */
    void createUser(CreateUserDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 检查用户密码是否正确
     * @param encryptPassword
     * @param request
     * @param result
     */
    void checkUserPwd(String encryptPassword, HttpServletRequest request, BaseResult result);

    /**
     * 修改管理员密码
     * @param dto
     * @param request
     * @param result
     */
    void updateUserPwd(UpdateUserPwdDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 修改用户信息
     * @param dto
     * @param request
     * @param result
     */
    void updateUserInfo(UpdateUserInfoDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取用户个人信息
     * @param request
     * @param result
     */
    void getUserDetailInfo(HttpServletRequest request, BaseResult result);

    /**
     * 获取个人资料信息
     * @param request
     * @param result
     */
    void getPersonal(HttpServletRequest request, BaseResult result);

    /**
     * 检查用户名是否存在
     * @param dto
     * @param request
     * @param result
     */
    void checkUserNameExist(CheckUsernameDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 修改用户个人信息
     * @param dto
     * @param request
     * @param result
     */
    void updateSelfInfo(UpdateSelfInfoDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 检查旧密码是否正确
     * @param dto
     * @param request
     * @param result
     */
    void checkOldPassword(CheckOldPasswordDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 修改用户密码
     * @param dto
     * @param request
     * @param result
     */
    void updatePassword(UpdatePwdDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取用户个人设置信息
     * @param request
     * @param result
     */
    void getSettingInfo(HttpServletRequest request, BaseResult result);

    /**
     * 获取用户发布列表
     * @param request
     * @param result
     */
    void getArticlePublishList(BaseRequest dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取用户收藏列表
     * @param request
     * @param result
     */
    void getArticleCollList(BaseRequest dto, HttpServletRequest request, BaseResult result);

    /**
     * 用户积分设置操作
     * @param dto
     * @param request
     * @param result
     */
    void operaPointSetting(UserPointSettingDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 用户打赏设置操作
     * @param dto
     * @param request
     * @param result
     */
    void operaRewardSetting(UserRewardSettingDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 更新用户打赏信息
     * @param dto
     * @param request
     * @param result
     */
    void updateRewardImgInfo(UserupdateRewardImgInfoDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 搜索用户
     * @param dto
     * @param request
     * @param result
     */
    void searchUserResult(KeyWordSearchDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 删除收藏
     * @param id
     * @param request
     * @param result
     */
    void removeArticleColl(Integer id, HttpServletRequest request, BaseResult result);

    /**
     * 用户中心删除自己的文章
     * @param articleId
     * @param request
     * @param result
     */
    void removeMyArticle(Integer articleId, HttpServletRequest request, BaseResult result);

    /**
     * 获取用户权限
     * @param userId
     * @param request
     * @param result
     */
    void getUserAuth(String userId, HttpServletRequest request, BaseResult result);

    /**
     * 修改用户权限
     * @param dto
     * @param request
     * @param result
     */
    void updateUserAuth(UpdateUserAuthDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取我的反馈
     * @param dto
     * @param request
     * @param result
     */
    void getFeedbackList(BaseRequest dto, HttpServletRequest request, BaseResult result);

    /**
     * 用户注册
     * @param dto
     * @param request
     * @param result
     */
    void register(UserRegirsterDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 用户邮件接收功能设置
     * @param dto
     * @param request
     * @param result
     */
    void operaReceiveEmailNoticeSetting(UserReceiveEmailNoticeSettingDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 邮箱是否绑定用户超过3个
     * @param dto
     * @param request
     * @param result
     */
    void checkEmailBindStatus(CheckEmailBindDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取积分交易流水
     * @param dto
     * @param request
     * @param result
     */
    void getPointTradeFlow(BaseRequest dto, HttpServletRequest request, BaseResult result);

    /**
     * 用户签到
     * @param request
     * @param result
     */
    void sign(HttpServletRequest request, BaseResult result);

    /**
     * 获取反馈详情
     * @param id
     * @param request
     * @param result
     */
    void getFeedbackInfo(Integer id, HttpServletRequest request, BaseResult result);

    /**
     * 获取用户举报列表
     * @param dto
     * @param request
     * @param result
     */
    void getReportList(BaseRequest dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取用户举报详情
     * @param id
     * @param request
     * @param result
     */
    void getReportInfo(Integer id, HttpServletRequest request, BaseResult result);

    /**
     * 获取用户下拉列表
     * @param dto
     * @param request
     * @param result
     */
    void adminGetSimpleUserList(GetSimpleUserListDTO dto, HttpServletRequest request, BaseResult result);

    /**
     * 获取用户订单列表
     * @param dto
     * @param request
     * @param result
     */
    void getUserOrderList(BaseRequest dto, HttpServletRequest request, BaseResult result);
}
