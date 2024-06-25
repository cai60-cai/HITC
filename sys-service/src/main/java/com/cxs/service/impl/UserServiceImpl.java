package com.cxs.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.auth.AuthUser;
import com.cxs.base.BasePageBean;
import com.cxs.base.BaseRequest;
import com.cxs.base.Token;
import com.cxs.base.UserSubject;
import com.cxs.base.BaseResult;
import com.cxs.client.LocationClient;
import com.cxs.client.req.LocationReq;
import com.cxs.client.resp.LocationResp;
import com.cxs.config.CommonConfig;
import com.cxs.config.SystemInnerConfig;
import com.cxs.constant.CachePrefixContent;
import com.cxs.constant.CommonContent;
import com.cxs.constant.ResponseStateConstant;
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
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.mapper.ArticleCollectionMapper;
import com.cxs.mapper.ArticleCommentMapper;
import com.cxs.mapper.ArticleMapper;
import com.cxs.mapper.FeedbackMapper;
import com.cxs.mapper.FeedbackReplyMapper;
import com.cxs.mapper.PointRechargeTypeMapper;
import com.cxs.mapper.PointTradeFlowMapper;
import com.cxs.mapper.PointTradeOrderMapper;
import com.cxs.mapper.ReportMapper;
import com.cxs.mapper.RoleMapper;
import com.cxs.mapper.UserAuthMapper;
import com.cxs.mapper.UserLoginLogMapper;
import com.cxs.mapper.UserRewardMapper;
import com.cxs.mapper.UserSettingMapper;
import com.cxs.model.Article;
import com.cxs.model.ArticleCollection;
import com.cxs.model.ArticleComment;
import com.cxs.model.Feedback;
import com.cxs.model.FeedbackReply;
import com.cxs.model.PointRechargeType;
import com.cxs.model.PointTradeFlow;
import com.cxs.model.PointTradeOrder;
import com.cxs.model.Report;
import com.cxs.model.Role;
import com.cxs.model.User;
import com.cxs.model.UserAuth;
import com.cxs.model.UserLoginLog;
import com.cxs.model.UserReward;
import com.cxs.model.UserRole;
import com.cxs.model.UserSetting;
import com.cxs.config.CompletableFutureService;
import com.cxs.service.MenuService;
import com.cxs.service.UserRoleService;
import com.cxs.service.UserService;
import com.cxs.mapper.UserMapper;
import com.cxs.utils.AesUtil;
import com.cxs.utils.JwtUtil;
import com.cxs.utils.RedisUtil;
import com.cxs.vo.admin.AdminLoginVO;
import com.cxs.vo.admin.menu.AdminMenuInfoVO;
import com.cxs.vo.admin.AdminUserVO;
import com.cxs.vo.admin.user.AdminUserViewVO;
import com.cxs.vo.admin.user.GetUserListVO;
import com.cxs.vo.admin.user.SearchUserVO;
import com.cxs.vo.admin.user.UserDetailVO;
import com.cxs.vo.user.PointTradeFlowVO;
import com.cxs.vo.user.UserArticleCollListVO;
import com.cxs.vo.user.UserArticlePublishListVO;
import com.cxs.vo.user.TokenCheckVO;
import com.cxs.vo.user.UserFeedbackInfoVO;
import com.cxs.vo.user.UserFeedbackListVO;
import com.cxs.vo.user.UserLoginLogVO;
import com.cxs.vo.user.UserLoginVO;
import com.cxs.vo.user.UserOrderListVO;
import com.cxs.vo.user.UserProfileVO;
import com.cxs.vo.user.UserReportArticleInfoVO;
import com.cxs.vo.user.UserReportCommentInfoVO;
import com.cxs.vo.user.UserReportInfoVO;
import com.cxs.vo.user.UserReportListVO;
import com.cxs.vo.user.UserSettingDetailVO;
import com.cxs.vo.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserLoginLogMapper userLoginLogMapper;

    @Autowired
    private LocationClient locationClient;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private MenuService menuService;

    @Autowired
    private SystemInnerConfig systemInnerConfig;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserSettingMapper userSettingMapper;

    @Autowired
    private UserRewardMapper userRewardMapper;

    @Autowired
    private CompletableFutureService completableFutureService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleCommentMapper articleCommentMapper;

    @Autowired
    private ArticleCollectionMapper articleCollectionMapper;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private FeedbackReplyMapper feedbackReplyMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private PointTradeFlowMapper pointTradeFlowMapper;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private PointTradeOrderMapper pointTradeOrderMapper;

    @Autowired
    private PointRechargeTypeMapper pointRechargeTypeMapper;

    @Autowired
    private AesUtil aesUtil;

    @Override
    public void login(UserLoginDTO dto, HttpServletRequest request, HttpServletResponse response, BaseResult result) {
        String accessTokenKey = request.getHeader(CommonContent.ACCESS_TOKEN);
        if (StringUtils.hasLength(accessTokenKey)) {
            String tokenStr = redisUtil.getString(redisUtil.getCacheKey(CachePrefixContent.TOKEN_PREFIX, accessTokenKey.trim()));
            Token token = null;
            try {
                token = jwtUtil.parseToken(tokenStr);
            } catch (Exception e) {
                log.info("用户登录:用户已有token校验失败");
            }
            if (!ObjectUtils.isEmpty(token)) {
                String generateToken = jwtUtil.generateToken(token.getUser());
                redisUtil.set(redisUtil.getCacheKey(CachePrefixContent.TOKEN_PREFIX, accessTokenKey), generateToken, commonConfig.getValidityTime(), TimeUnit.MINUTES);
                Token parseToken = jwtUtil.parseToken(generateToken);
                if (!ObjectUtils.isEmpty(parseToken)) parseToken.setToken(accessTokenKey);
                Cookie loginCookie = new Cookie(CommonContent.ACCESS_TOKEN, accessTokenKey);
                loginCookie.setPath("/");
                loginCookie.setDomain(commonConfig.getDomian());
                loginCookie.setMaxAge(commonConfig.getValidityTime().intValue() * 60);
                response.addCookie(loginCookie);
                UserLoginVO vo = new UserLoginVO();
                vo.setTokenInfo(parseToken);
                UserVO userVO = new UserVO();
                User userInfo = userMapper.selectById(parseToken.getUser().getId());
                BeanUtils.copyProperties(userInfo, userVO);
                userVO.setRoles(parseToken.getUser().getAuthentications());
                vo.setUser(userVO);
                // 记录日志
                String address = null;
                LocationResp locationResp = locationClient.getAddr(LocationReq.builder().ip(getIpAddr(request)).build());
                if (!ObjectUtils.isEmpty(locationResp) && locationResp.getStatus() == 0) {
                    address = locationResp.getContent().getAddress();
                }
                UserLoginLog loginLog = UserLoginLog.builder()
                        .loginMode(1)
                        .loginTime(LocalDateTime.now())
                        .loginIp(getIpAddr(request))
                        .userId(userInfo.getUserId())
                        .loginAddress(address)
                        .build();
                int insert = userLoginLogMapper.insert(loginLog);
                if (insert == 1) {
                    result.setCode(ResponseStateConstant.OPERA_SUCCESS).setData(vo).setMsg("登陆成功");
                } else {
                    result.setCode(ResponseStateConstant.OPERA_FAIL).setMsg("登陆失败").setData(new JSONObject());
                }
                return;
            }
        }
        String encryptPassword = dto.getPassword().trim();
        String password = aesUtil.aesDecrypt(encryptPassword);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getUsername().trim(), password);
        Authentication authenticate = null;
        try {
            System.out.println("sssssssssssssssss");
            System.out.println("sssssssssssssssss");
            System.out.println("sssssssssssssssss");
            System.out.println("sssssssssssssssss");
            authenticate = authenticationManager.authenticate(token);
        } catch (AuthenticationException e) {
            System.out.println("sssssssssssssssss");
            System.out.println("sssssssssssssssss");
            System.out.println("sssssssssssssssss");

            e.printStackTrace();
            result.setCode(ResponseStateConstant.OPERA_FAIL).setMsg(e.getMessage());
        }
        if (authenticate != null) {
            UserLoginVO vo = new UserLoginVO();
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            Object principal = authenticate.getPrincipal();
            AuthUser user = (AuthUser) principal;
            List<String> auths = CollectionUtils.isEmpty(user.getAuthorities()) ? new ArrayList<>(0) :
                    user.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());
            String tokenStr = IdUtil.simpleUUID();
            String generateToken = jwtUtil.generateToken(UserSubject.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .authentications(auths).build());
            redisUtil.set(redisUtil.getCacheKey(CachePrefixContent.TOKEN_PREFIX, tokenStr), generateToken, commonConfig.getValidityTime(), TimeUnit.MINUTES);
            Token parseToken = jwtUtil.parseToken(generateToken);
            Cookie loginCookie = new Cookie(CommonContent.ACCESS_TOKEN, tokenStr);
            loginCookie.setPath("/");
            loginCookie.setDomain(commonConfig.getDomian());
            loginCookie.setMaxAge(commonConfig.getValidityTime().intValue() * 60);
            response.addCookie(loginCookie);
            if (!ObjectUtils.isEmpty(parseToken)) parseToken.setToken(tokenStr);
            vo.setTokenInfo(parseToken);
            UserVO userVO = new UserVO();
            User userInfo = userMapper.selectById(user.getId());
            BeanUtils.copyProperties(userInfo, userVO);
            userVO.setRoles(auths);
            vo.setUser(userVO);
            // 记录日志
            String address = null;
            LocationResp locationResp = locationClient.getAddr(LocationReq.builder().ip(getIpAddr(request)).build());
            if (!ObjectUtils.isEmpty(locationResp) && locationResp.getStatus() == 0) {
                address = locationResp.getContent().getAddress();
            }
            UserLoginLog loginLog = UserLoginLog.builder()
                    .loginMode(1)
                    .loginTime(LocalDateTime.now())
                    .loginIp(getIpAddr(request))
                    .userId(userInfo.getUserId())
                    .loginAddress(address)
                    .build();
            int insert = userLoginLogMapper.insert(loginLog);
            if (insert == 1) {
                result.setCode(ResponseStateConstant.OPERA_SUCCESS).setData(vo).setMsg("登陆成功");
            } else {
                System.out.println("sssssssssssssssss");
                System.out.println("sssssssssssssssss");
                System.out.println("sssssssssssssssss");
                result.setCode(ResponseStateConstant.OPERA_FAIL).setMsg("登陆失败").setData(new JSONObject());
            }
        }
    }

    @Override
    public void checkToken(HttpServletRequest request, BaseResult result) {
        String accessTokenKey = request.getHeader(CommonContent.ACCESS_TOKEN);
        if (!StringUtils.hasLength(accessTokenKey)) {
            Cookie[] cookies = request.getCookies();
            if (ObjectUtils.isEmpty(cookies) || cookies.length == 0) {
                result.setCode(ResponseStateConstant.NO_LOGIN).setMsg("access_token缺失");
                return;
            } else {
                for (Cookie cookie : cookies) {
                    if (CommonContent.ACCESS_TOKEN.equals(cookie.getName())) {
                        accessTokenKey = cookie.getValue();
                    }
                }
            }
        }
        if (!StringUtils.hasLength(accessTokenKey)) {
            result.setCode(ResponseStateConstant.NO_LOGIN).setMsg("access_token缺失");
            return;
        }
        TokenCheckVO vo = new TokenCheckVO();
        String tokenStr = redisUtil.getString(redisUtil.getCacheKey(CachePrefixContent.TOKEN_PREFIX, accessTokenKey.trim()));
        if (!StringUtils.hasLength(tokenStr)) {
            vo.setStatus(Boolean.FALSE);
            vo.setToken(null);
            result.setCode(CurrencyErrorEnum.UNAUTHORIZED_BE_OVERDUE.getCode())
                    .setData(vo)
                    .setMsg(CurrencyErrorEnum.UNAUTHORIZED_BE_OVERDUE.getMsg());
        } else {
            Token token = jwtUtil.parseToken(tokenStr);
            if (!ObjectUtils.isEmpty(token)) {
                token.setToken(accessTokenKey);
                vo.setStatus(Boolean.TRUE);
                vo.setToken(token);
            }
            result.setCode(ResponseStateConstant.OPERA_SUCCESS).setData(vo);
        }
    }

    @Override
    public void checkToken(String token, BaseResult result) {
        String accessTokenKey = token;
        if (!StringUtils.hasLength(accessTokenKey)) {
            result.setCode(ResponseStateConstant.NO_LOGIN).setMsg("access_token缺失");
            return;
        }
        TokenCheckVO vo = new TokenCheckVO();
        String tokenStr = redisUtil.getString(redisUtil.getCacheKey(CachePrefixContent.TOKEN_PREFIX, accessTokenKey.trim()));
        if (!StringUtils.hasLength(tokenStr)) {
            vo.setStatus(Boolean.FALSE);
            vo.setToken(null);
            result.setCode(CurrencyErrorEnum.UNAUTHORIZED_BE_OVERDUE.getCode())
                    .setData(vo)
                    .setMsg(CurrencyErrorEnum.UNAUTHORIZED_BE_OVERDUE.getMsg());
        } else {
            Token tokenVO = jwtUtil.parseToken(tokenStr);
            if (!ObjectUtils.isEmpty(token)) {
                tokenVO.setToken(accessTokenKey);
                vo.setStatus(Boolean.TRUE);
                vo.setToken(tokenVO);
            }
            result.setCode(ResponseStateConstant.OPERA_SUCCESS).setData(vo);
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, BaseResult result) {
        String header = request.getHeader(CommonContent.ACCESS_TOKEN);
        if (StringUtils.hasLength(header)) {
            redisUtil.removeKey(redisUtil.getCacheKey(CachePrefixContent.TOKEN_PREFIX, header.trim()));
            result.setCode(ResponseStateConstant.OPERA_SUCCESS).setMsg("操作成功");
        }
        Cookie cookie = new Cookie(CommonContent.ACCESS_TOKEN, null);
        cookie.setPath("/");
        cookie.setDomain(commonConfig.getDomian());
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

    @Override
    public void adminLogin(UserLoginDTO dto, HttpServletRequest request, BaseResult result) {
        String accessTokenKey = request.getHeader(CommonContent.ACCESS_TOKEN);
        if (StringUtils.hasLength(accessTokenKey)) {
            String tokenStr = redisUtil.getString(redisUtil.getCacheKey(CachePrefixContent.TOKEN_PREFIX, accessTokenKey.trim()));
            Token token = null;
            try {
                token = jwtUtil.parseToken(tokenStr);
            } catch (Exception e) {
                log.info("用户登录:用户已有token校验失败");
            }
            if (!ObjectUtils.isEmpty(token)) {
                List<String> roleList = token.getUser().getAuthentications();
                if (CollectionUtils.isEmpty(roleList) || !(roleList.contains(CommonContent.SUPER_ADMIN_FLAG) || roleList.contains(CommonContent.ADMIN_FLAG))) {
                    result.setCode(CurrencyErrorEnum.ADMIN_LOGIN_FORBIDDEN.getCode()).setMsg(CurrencyErrorEnum.ADMIN_LOGIN_FORBIDDEN.getMsg());
                } else {
                    String generateToken = jwtUtil.generateToken(token.getUser());
                    redisUtil.set(redisUtil.getCacheKey(CachePrefixContent.TOKEN_PREFIX, accessTokenKey), generateToken, commonConfig.getValidityTime(), TimeUnit.MINUTES);
                    Token parseToken = jwtUtil.parseToken(generateToken);
                    if (!ObjectUtils.isEmpty(parseToken)) parseToken.setToken(accessTokenKey);
                    AdminLoginVO vo = new AdminLoginVO();
                    vo.setTokenInfo(parseToken);
                    AdminUserVO userVO = new AdminUserVO();
                    User userInfo = userMapper.selectById(parseToken.getUser().getId());
                    BeanUtils.copyProperties(userInfo, userVO);
                    userVO.setRoles(parseToken.getUser().getAuthentications().stream().map(p -> p.replace("ROLE_", "")).collect(Collectors.toList()));
                    AdminMenuInfoVO menuInfoVO = new AdminMenuInfoVO();
                    if (parseToken.getUser().getAuthentications().contains(systemInnerConfig.getSuperAdminRoleName())) {
                        menuInfoVO.setPermissions(this.menuService.getAdminMenuListByUserId(parseToken.getUser().getId(), Boolean.TRUE));
                    } else {
                        menuInfoVO.setPermissions(this.menuService.getAdminMenuListByUserId(parseToken.getUser().getId(), Boolean.FALSE));
                    }
                    userVO.setMenuInfo(menuInfoVO);
                    vo.setUserInfo(userVO);
                    // 记录日志
                    String address = null;
                    LocationResp locationResp = locationClient.getAddr(LocationReq.builder().ip(getIpAddr(request)).build());
                    if (!ObjectUtils.isEmpty(locationResp) && locationResp.getStatus() == 0) {
                        address = locationResp.getContent().getAddress();
                    }
                    UserLoginLog loginLog = UserLoginLog.builder()
                            .loginMode(2)
                            .loginTime(LocalDateTime.now())
                            .loginIp(getIpAddr(request))
                            .userId(userInfo.getUserId())
                            .loginAddress(address)
                            .build();
                    int insert = userLoginLogMapper.insert(loginLog);
                    if (insert == 1) {
                        result.setCode(ResponseStateConstant.OPERA_SUCCESS).setData(vo).setMsg("登陆成功");
                    } else {
                        result.setCode(ResponseStateConstant.OPERA_FAIL).setMsg("登陆失败").setData(new JSONObject());
                    }
                    return;
                }
            }
        }
        String encryptPassword = dto.getPassword().trim();
        String password = aesUtil.aesDecrypt(encryptPassword);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getUsername().trim(), password);
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            result.setCode(ResponseStateConstant.OPERA_FAIL).setMsg(e.getMessage());
        }
        if (authenticate != null) {
            AdminLoginVO vo = new AdminLoginVO();
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            Object principal = authenticate.getPrincipal();
            AuthUser user = (AuthUser) principal;
            List<String> auths = CollectionUtils.isEmpty(user.getAuthorities()) ? new ArrayList<>(0) :
                    user.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(auths) || !(auths.contains(CommonContent.SUPER_ADMIN_FLAG) || auths.contains(CommonContent.ADMIN_FLAG))) {
                result.setCode(CurrencyErrorEnum.ADMIN_LOGIN_FORBIDDEN.getCode()).setMsg(CurrencyErrorEnum.ADMIN_LOGIN_FORBIDDEN.getMsg());
            } else {
                String tokenStr = IdUtil.simpleUUID();
                String generateToken = jwtUtil.generateToken(UserSubject.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .authentications(auths).build());
                redisUtil.set(redisUtil.getCacheKey(CachePrefixContent.TOKEN_PREFIX, tokenStr), generateToken, commonConfig.getValidityTime(), TimeUnit.MINUTES);
                Token parseToken = jwtUtil.parseToken(generateToken);
                if (!ObjectUtils.isEmpty(parseToken)) parseToken.setToken(tokenStr);
                vo.setTokenInfo(parseToken);
                AdminUserVO userVO = new AdminUserVO();
                User userInfo = userMapper.selectById(parseToken.getUser().getId());
                BeanUtils.copyProperties(userInfo, userVO);
                userVO.setRoles(parseToken.getUser().getAuthentications().stream().map(p -> p.replace("ROLE_", "")).collect(Collectors.toList()));
                AdminMenuInfoVO menuInfoVO = new AdminMenuInfoVO();
                if (parseToken.getUser().getAuthentications().contains(systemInnerConfig.getSuperAdminRoleName())) {
                    menuInfoVO.setPermissions(this.menuService.getAdminMenuListByUserId(parseToken.getUser().getId(), Boolean.TRUE));
                } else {
                    menuInfoVO.setPermissions(this.menuService.getAdminMenuListByUserId(parseToken.getUser().getId(), Boolean.FALSE));
                }
                userVO.setMenuInfo(menuInfoVO);
                vo.setUserInfo(userVO);
                // 记录日志
                String address = null;
                LocationResp locationResp = locationClient.getAddr(LocationReq.builder().ip(getIpAddr(request)).build());
                if (!ObjectUtils.isEmpty(locationResp) && locationResp.getStatus() == 0) {
                    address = locationResp.getContent().getAddress();
                }
                UserLoginLog loginLog = UserLoginLog.builder()
                        .loginMode(2)
                        .loginTime(LocalDateTime.now())
                        .loginIp(getIpAddr(request))
                        .userId(userInfo.getUserId())
                        .loginAddress(address)
                        .build();
                int insert = userLoginLogMapper.insert(loginLog);
                if (insert == 1) {
                    result.setCode(ResponseStateConstant.OPERA_SUCCESS).setData(vo).setMsg("登陆成功");
                } else {
                    result.setCode(ResponseStateConstant.OPERA_FAIL).setMsg("登陆失败").setData(new JSONObject());
                }
            }
        }
    }

    @Override
    public void getUserLoginLog(BaseRequest dto, HttpServletRequest request, BaseResult result) {
        UserSubject userByToken = getUserByToken(request);
        BasePageBean pageBean = new BasePageBean();
        Page<UserLoginLog> page = new Page<>();
        page.setCurrent(Long.parseLong(dto.getPageNum().toString()));
        page.setSize(Long.parseLong(dto.getPageSize().toString()));
        LambdaQueryWrapper<UserLoginLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLoginLog::getUserId, userByToken.getId()).orderByDesc(UserLoginLog::getLoginTime);
        IPage<UserLoginLog> selectPage = userLoginLogMapper.selectPage(page, wrapper);
        pageBean.setPageNum(selectPage.getCurrent());
        pageBean.setPageSize(selectPage.getSize());
        pageBean.setPages(selectPage.getPages());
        pageBean.setTotal(selectPage.getTotal());
        pageBean.setData(
                CollectionUtils.isEmpty(selectPage.getRecords()) ? new ArrayList<>(0):
                        selectPage.getRecords().stream().map(l -> {
                            UserLoginLogVO vo = new UserLoginLogVO();
                            BeanUtils.copyProperties(l, vo);
                            return vo;
                        }).collect(Collectors.toList())
        );
        result.setData(pageBean);
    }

    @Override
    public UserSubject getUserByToken(HttpServletRequest request) {
        BaseResult result = BaseResult.ok();
        try {
            this.checkToken(request, result);
            if (result.getCode().equals(ResponseStateConstant.OPERA_SUCCESS)) {
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(result.getData()));
                if (jsonObject.containsKey("token") && jsonObject.getBoolean("status")) {
                    JSONObject token = jsonObject.getJSONObject("token");
                    return JSON.parseObject(token.getJSONObject("user").toJSONString(), UserSubject.class);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserSubject getUserByToken(String token) {
        BaseResult result = BaseResult.ok();
        try {
            this.checkToken(token, result);
            if (result.getCode().equals(ResponseStateConstant.OPERA_SUCCESS)) {
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(result.getData()));
                if (jsonObject.containsKey("token") && jsonObject.getBoolean("status")) {
                    JSONObject tokenVO = jsonObject.getJSONObject("token");
                    return JSON.parseObject(tokenVO.getJSONObject("user").toJSONString(), UserSubject.class);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void getUserInfo(HttpServletRequest request, BaseResult result) {
        AdminUserVO userVO = new AdminUserVO();
        UserSubject token = getUserByToken(request);
        if (ObjectUtils.isEmpty(token)) {
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户登录信息已失效");
            return;
        }
        User user = userMapper.selectById(token.getId());
        BeanUtils.copyProperties(user, userVO);
        List<Role> roles = roleMapper.selectRoleByUserId(user.getUserId());
        List<String> roleList = CollectionUtils.isEmpty(roles) ? new ArrayList<>(0) :
                roles.stream().map(Role::getRoleName).collect(Collectors.toList());
        userVO.setRoles(roleList);
        result.setData(userVO);

    }

    @Override
    public void getUserList(GetUserListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            IPage<User> page = new Page<>(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            String keyWord = dto.getKeyWord();
            wrapper.orderByAsc(User::getCreateTime);
            if (StringUtils.hasLength(keyWord)) {
                wrapper.like(User::getUserName, keyWord)
                        .or().like(User::getNickName, keyWord)
                        .or().like(User::getPhone, keyWord)
                        .or().like(User::getUserStatus, keyWord)
                        .or().like(User::getEmail, keyWord);
            }
            IPage<User> userIPage = userMapper.selectPage(page, wrapper);
            BasePageBean pageBean = BasePageBean.builder()
                    .pageNum(userIPage.getCurrent())
                    .pageSize(userIPage.getSize())
                    .pages(userIPage.getPages())
                    .total(userIPage.getTotal())
                    .data(
                            CollectionUtils.isEmpty(userIPage.getRecords()) ? new ArrayList<>(0) :
                                    userIPage.getRecords().stream().map(u -> {
                                        GetUserListVO vo = new GetUserListVO();
                                        BeanUtils.copyProperties(u, vo);
                                        return vo;
                                    }).collect(Collectors.toList()))
                    .build();
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("获取用户列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    @Override
    public void updateStatus(UpdateStatusDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            if (userByToken.getId().equals(dto.getUserId().trim())) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",禁止自己操作自己");
                return;
            }
            if (systemInnerConfig.getInnerSuperAdminList().contains(dto.getUserId())) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",禁止操作系统超级管理员");
                return;
            }
            User user = new User();
            user.setUserId(dto.getUserId());
            user.setUserStatus(dto.getStatus());
            int update = userMapper.updateById(user);
            if (update != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
            }
        } catch (Exception e) {
            log.error("修改用户状态失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【修改用户状态接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void resetUserPwd(String userId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            User user = new User();
            user.setUserId(userId);
            user.setPassword(passwordEncoder.encode(commonConfig.getDefaultPwd()));
            int update = userMapper.updateById(user);
            if (update != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
            }
        } catch (Exception e) {
            log.error("重置用户密码失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【重置用户密码接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, userId, result);
        }
    }

    @Override
    public void deleteUser(String userId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            if (userByToken.getId().equals(userId)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",禁止自己操作自己");
                return;
            }
            if (systemInnerConfig.getInnerUserList().contains(userId)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",内置系统用户禁止删除");
                return;
            }
            User user = userMapper.selectById(userId);
            if (ObjectUtils.isEmpty(user)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户不存在");
                return;
            }
            int update = userMapper.deleteById(userId);
            if (update != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
            }
        } catch (Exception e) {
            log.error("用户删除失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户删除接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, userId, result);
        }
    }

    @Override
    public void createUser(CreateUserDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            User user = new User();
            user.setUserId(IdUtil.simpleUUID());
            user.setUserName(dto.getUserName().trim());
            user.setPassword(passwordEncoder.encode(commonConfig.getDefaultPwd()));
            user.setCreateTime(LocalDateTime.now());
            int insert = 0;
            try {
                insert = userMapper.insert(user);
            } catch (DuplicateKeyException e) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户名已存在");
                return;
            }
            if (insert == 1) {
                List<Integer> roleIds = dto.getRoleIds();
                if (!CollectionUtils.isEmpty(roleIds)) {
                    List<UserRole> list = new ArrayList<>();
                    for (Integer roleId : roleIds) {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(user.getUserId());
                        userRole.setRoleId(roleId);
                        list.add(userRole);
                    }
                    boolean res = this.userRoleService.saveBatch(list);
                    if (res) {
                        result.setMsg("用户创建成功");
                        UserSetting setting = new UserSetting();
                        setting.setUserId(user.getUserId());
                        UserAuth userAuth = new UserAuth();
                        userAuth.setUserId(user.getUserId());
                        // 创建用户设置
                        userSettingMapper.insert(setting);
                        // 创建用户权限
                        userAuthMapper.insert(userAuth);
                    } else {
                        result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                    }
                }
            } else {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
            }
        } catch (Exception e) {
            log.error("创建用户失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【创建用户接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, dto, result);
        }
    }

    @Override
    public void checkUserPwd(String encryptPassword, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject token = getUserByToken(request);
            User user = userMapper.selectById(token.getId());
            String password = aesUtil.aesDecrypt(encryptPassword);
            if (passwordEncoder.matches(password, user.getPassword())) {
                result.setData(Boolean.TRUE);
            } else {
                result.setData(Boolean.FALSE);
            }
        } catch (Exception e) {
            log.error("检查用户密码失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【检查用户密码接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, encryptPassword, result);
        }
    }

    @Override
    public void updateUserPwd(UpdateUserPwdDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject token = getUserByToken(request);
            User user = userMapper.selectById(token.getId());
            String password = aesUtil.aesDecrypt(dto.getOldPwd().trim());
            String newPassword = aesUtil.aesDecrypt(dto.getNewPwd().trim());
            if (passwordEncoder.matches(password, user.getPassword())) {
                User updateUser = new User();
                updateUser.setUserId(user.getUserId());
                updateUser.setUpdateTime(LocalDateTime.now());
                updateUser.setPassword(passwordEncoder.encode(newPassword));
                int update = userMapper.updateById(updateUser);
                if (update != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                }
            } else {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",旧密码不正确");
            }
        } catch (Exception e) {
            log.error("修改用户密码失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【修改用户密码接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void updateUserInfo(UpdateUserInfoDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject token = getUserByToken(request);
            User user = new User();
            BeanUtils.copyProperties(dto, user);
            user.setUserId(token.getId());
            user.setUpdateTime(LocalDateTime.now());
            int update = userMapper.updateById(user);
            if (update != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
            }
        } catch (Exception e) {
            log.error("修改用户信息失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【修改用户信息接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void getUserDetailInfo(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserDetailVO userVO = new UserDetailVO();
            UserSubject token = getUserByToken(request);
            User user = userMapper.selectById(token.getId());
            BeanUtils.copyProperties(user, userVO);
            result.setData(userVO);
        } catch (Exception e) {
            log.error("获取用户信息失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户信息接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void getPersonal(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserProfileVO userVO = new UserProfileVO();
            UserSubject token = getUserByToken(request);
            User user = userMapper.selectById(token.getId());
            BeanUtils.copyProperties(user, userVO);
            // 获取用户最后一次登陆地点
            LambdaQueryWrapper<UserLoginLog> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserLoginLog::getUserId, user.getUserId())
                    .orderByDesc(UserLoginLog::getLoginTime).last("limit 1");
            UserLoginLog loginLog = userLoginLogMapper.selectOne(queryWrapper);
            if (!ObjectUtils.isEmpty(loginLog)) {
                userVO.setAddress(loginLog.getLoginAddress());
            }
            result.setData(userVO);
        } catch (Exception e) {
            log.error("获取用户个人信息信息失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户个人信息接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void checkUserNameExist(CheckUsernameDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUserName, dto.getUserName()).select(User::getUserId);
            List<User> userList = userMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(userList)) {
                result.setData(Boolean.FALSE);
            } else {
                if (!ObjectUtils.isEmpty(userByToken)) {
                    User user = userList.get(0);
                    if (userByToken.getId().equals(user.getUserId())) {
                        result.setData(Boolean.FALSE);
                    } else {
                        result.setData(Boolean.TRUE);
                    }
                } else {
                    result.setData(Boolean.TRUE);
                }
            }
        } catch (Exception e) {
            log.error("验证用户名是否存在失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【验证用户名是否存在接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    @Override
    public void updateSelfInfo(UpdateSelfInfoDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            User user = new User();
            BeanUtils.copyProperties(dto, user);
            user.setUserId(userByToken.getId());
            user.setUpdateTime(LocalDateTime.now());
            user.setUserType(2);
            int update = userMapper.updateById(user);
            if (update != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
            }
        } catch (Exception e) {
            log.error("修改用户个人信息失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【修改用户个人信息接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void checkOldPassword(CheckOldPasswordDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUserName, userByToken.getUsername()).select(User::getUserId, User::getPassword);
            List<User> userList = userMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(userList)) {
                result.setData(Boolean.FALSE);
            } else {
                User user = userList.get(0);
                String password = aesUtil.aesDecrypt(dto.getOldPassword());
                result.setData(passwordEncoder.matches(password, user.getPassword()));
            }
        } catch (Exception e) {
            log.error("验证旧密码是否正确失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【验证旧密码是否正确接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    @Override
    public void updatePassword(UpdatePwdDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            String newPassword = aesUtil.aesDecrypt(dto.getPassword());
            if (newPassword.length() > 16) {
                result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",用户密码最大16个字符");
                return;
            }
            UserSubject userByToken = getUserByToken(request);
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUserName, userByToken.getUsername()).select(User::getUserId, User::getPassword);
            List<User> userList = userMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(userList)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",密码修改失败");
            } else {
                User user = userList.get(0);
                String password = aesUtil.aesDecrypt(dto.getOldPassword());
                if (passwordEncoder.matches(password, user.getPassword())) {
                    User updateUser = new User();
                    updateUser.setUserId(user.getUserId());
                    updateUser.setPassword(passwordEncoder.encode(newPassword));
                    updateUser.setUpdateTime(LocalDateTime.now());
                    int update = userMapper.updateById(updateUser);
                    if (update != 1) {
                        result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                    }
                } else {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",密码修改失败,旧密码不正确");
                }
            }
        } catch (Exception e) {
            log.error("修改密码失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【修改密码接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void getSettingInfo(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            UserSetting userSetting = userSettingMapper.selectById(userByToken.getId());
            UserSettingDetailVO vo = new UserSettingDetailVO();
            if (!ObjectUtils.isEmpty(userSetting)) {
                BeanUtils.copyProperties(userSetting, vo);
                // 获取用户打赏详细配置信息
                UserReward userReward = userRewardMapper.selectById(userByToken.getId());
                if (!ObjectUtils.isEmpty(userReward)) {
                    vo.setRewardInfo(JSON.parseObject(JSON.toJSONString(userReward)));
                } else {
                    vo.setRewardInfo(new JSONObject());
                }
            }
            result.setData(vo);
        } catch (Exception e) {
            log.error("获取用户个人设置失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户个人设置接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void getArticlePublishList(BaseRequest dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            BasePageBean pageBean = new BasePageBean();
            Page page = new Page(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Article::getArticleBelongUserId, userByToken.getId()).select(Article::getArticleId, Article::getArticleTitle, Article::getArticleStatus, Article::getArticleIsSelf, Article::getArticleRate, Article::getArticleDesc).orderByDesc(Article::getCreateTime);
            IPage<Article> iPage = articleMapper.selectPage(page, queryWrapper);
            List<Article> articleList = iPage.getRecords();
            List<UserArticlePublishListVO> voList = CollectionUtils.isEmpty(articleList) ? new ArrayList<>(0) : articleList.stream().map(a -> {
                UserArticlePublishListVO vo = new UserArticlePublishListVO();
                BeanUtils.copyProperties(a, vo);
                return vo;
            }).collect(Collectors.toList());
            pageBean.setPageNum(iPage.getCurrent());
            pageBean.setPageSize(iPage.getSize());
            pageBean.setPages(iPage.getPages());
            pageBean.setTotal(iPage.getTotal());
            pageBean.setData(voList);
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("获取用户发布列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户发布列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void getArticleCollList(BaseRequest dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            BasePageBean pageBean = new BasePageBean();
            Page page = new Page(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<ArticleCollection> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ArticleCollection::getUserId, userByToken.getId()).orderByDesc(ArticleCollection::getCollectionTime);
            IPage<ArticleCollection> iPage = articleCollectionMapper.selectPage(page, queryWrapper);
            List<ArticleCollection> collectionList = iPage.getRecords();
            List<UserArticleCollListVO> voList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(collectionList)) {
                List<Integer> articleIds = collectionList.stream().map(ArticleCollection::getArticleId).collect(Collectors.toList());
                Set<String> userIds = collectionList.stream().map(ArticleCollection::getBelongUserId).collect(Collectors.toSet());
                LambdaQueryWrapper<Article> articleQuery = new LambdaQueryWrapper<>();
                articleQuery.eq(Article::getArticleStatus, 1).in(Article::getArticleId, articleIds).select(Article::getArticleId, Article::getArticleTitle, Article::getArticleStatus, Article::getArticleIsSelf, Article::getArticleRate);
                List<Article> articleList = articleMapper.selectList(articleQuery);
                Map<Integer, Article> articleMap = articleList.stream().collect(Collectors.toMap(Article::getArticleId, Function.identity(), (o1, o2) -> o1));

                LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userLambdaQueryWrapper.select(User::getUserId, User::getNickName).in(User::getUserId, userIds);
                List<User> userList = userMapper.selectList(userLambdaQueryWrapper);
                Map<String, User> userMap = userList.stream().collect(Collectors.toMap(User::getUserId, Function.identity(), (o1, o2) -> o1));

                for (ArticleCollection articleCollection : collectionList) {
                    UserArticleCollListVO vo = new UserArticleCollListVO();
                    vo.setId(articleCollection.getId());
                    User user = userMap.get(articleCollection.getBelongUserId());
                    vo.setAuthor(ObjectUtils.isEmpty(user) ? "" : user.getNickName());
                    Article article = articleMap.get(articleCollection.getArticleId());
                    vo.setStatus(ObjectUtils.isEmpty(article) ? -1 : 1);
                    if (!ObjectUtils.isEmpty(article)) {
                        BeanUtils.copyProperties(article, vo);
                    }
                    voList.add(vo);
                }
            }
            pageBean.setPageNum(iPage.getCurrent());
            pageBean.setPageSize(iPage.getSize());
            pageBean.setPages(iPage.getPages());
            pageBean.setTotal(iPage.getTotal());
            pageBean.setData(voList);
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("获取用户收藏列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户收藏列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void operaPointSetting(UserPointSettingDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            UserSetting userSetting = new UserSetting();
            userSetting.setUserId(userByToken.getId());
            userSetting.setShowPoint(dto.getShowPoint());
            int update = userSettingMapper.updateById(userSetting);
            if (update != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
            }
        } catch (Exception e) {
            log.error("用户积分设置操作失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户积分设置操作接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void operaRewardSetting(UserRewardSettingDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            UserAuth userAuth = userAuthMapper.selectById(userByToken.getId());
            if (dto.getOpenReward()) {
                if (!userAuth.getRewardAuth()) {
                    result.setCode(CurrencyErrorEnum.AUTH_LOCK.getCode());
                    result.setMsg(CurrencyErrorEnum.AUTH_LOCK.getMsg() + ",暂无法开通打赏功能,请联系管理员");
                } else {
                    UserSetting userSetting = new UserSetting();
                    userSetting.setUserId(userByToken.getId());
                    userSetting.setOpenReward(dto.getOpenReward());
                    int update = userSettingMapper.updateById(userSetting);
                    if (update != 1) {
                        result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                    }
                }
            } else {
                UserSetting userSetting = new UserSetting();
                userSetting.setUserId(userByToken.getId());
                userSetting.setOpenReward(dto.getOpenReward());
                int update = userSettingMapper.updateById(userSetting);
                if (update != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                }
            }

        } catch (Exception e) {
            log.error("用户打赏设置操作失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户打赏设置操作接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void updateRewardImgInfo(UserupdateRewardImgInfoDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            UserReward reward = new UserReward();
            BeanUtils.copyProperties(dto, reward);
            reward.setUserId(userByToken.getId());
            int update = userRewardMapper.updateIfExistInsert(reward);
            if (update <= 0) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
            }
        } catch (Exception e) {
            log.error("更新用户打赏信息失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【更新用户打赏信息接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void searchUserResult(KeyWordSearchDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            String keyWord = dto.getKeyWord();
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasLength(keyWord)) {
                queryWrapper.select(User::getUserId, User::getNickName).like(User::getUserName, keyWord)
                        .or().like(User::getNickName, keyWord);
            } else {
                queryWrapper.select(User::getUserId, User::getNickName).last("limit 10");
            }
            List<User> users = userMapper.selectList(queryWrapper);
            List<SearchUserVO> voList = CollectionUtils.isEmpty(users) ? new ArrayList<>(0) : users.stream().map(u -> {
                SearchUserVO vo = new SearchUserVO();
                BeanUtils.copyProperties(u, vo);
                return vo;
            }).collect(Collectors.toList());
            result.setData(voList);
        } catch (Exception e) {
            log.error("搜索用户失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【搜索用户接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void removeArticleColl(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            ArticleCollection articleCollection = articleCollectionMapper.selectById(id);
            if (ObjectUtils.isEmpty(articleCollection)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",收藏内容不存在");
            } else {
                if (userByToken.getId().equals(articleCollection.getUserId())) {
                    articleCollectionMapper.deleteById(id);
                } else {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",权限不足");
                }
            }
        } catch (Exception e) {
            log.error("用户删除收藏列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户删除收藏列表接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, id, result);
        }
    }

    @Override
    public void removeMyArticle(Integer articleId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            Article article = articleMapper.selectById(articleId);
            if (ObjectUtils.isEmpty(article)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该文章不存在");
            } else {
                if (userByToken.getId().equals(article.getArticleBelongUserId())) {
                    if (article.getArticleStatus().equals(1)) {
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",已发布的文章不可删除");
                    } else {
                        // 删除文章
                        articleMapper.deleteById(articleId);
                    }
                } else {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",权限不足,只允许删除本人的文章");
                }
            }
        } catch (Exception e) {
            log.error("用户删除自己的文章失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户删除自己的文章接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, articleId, result);
        }
    }

    @Override
    public void getUserAuth(String userId, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserAuth userAuth = userAuthMapper.selectById(userId);
            if (ObjectUtils.isEmpty(userAuth)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户不存在");
            } else {
                result.setData(userAuth);
            }
        } catch (Exception e) {
            log.error("管理员-获取用户权限失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员-获取用户权限接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, userId, result);
        }
    }

    @Override
    public void updateUserAuth(UpdateUserAuthDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            if (userByToken.getId().equals(dto.getUserId().trim())) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",禁止自己操作自己");
                return;
            }
            if (systemInnerConfig.getInnerSuperAdminList().contains(dto.getUserId())) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",禁止操作系统超级管理员");
                return;
            }
            UserAuth userAuth = userAuthMapper.selectById(dto.getUserId());
            if (ObjectUtils.isEmpty(userAuth)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户不存在");
            } else {
                UserAuth auth = new UserAuth();
                BeanUtils.copyProperties(dto, auth);
                int update = userAuthMapper.updateById(auth);
                if (update != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                }
                if (!dto.getRewardAuth()) {
                    UserSetting userSetting = new UserSetting();
                    userSetting.setUserId(dto.getUserId());
                    userSetting.setOpenReward(Boolean.FALSE);
                    int settings = userSettingMapper.updateById(userSetting);
                    if (settings != 1) {
                        result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                    }
                }
            }
        } catch (Exception e) {
            log.error("管理员-修改用户权限失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员-修改用户权限接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void getFeedbackList(BaseRequest dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            BasePageBean pageBean = new BasePageBean();
            Page page = new Page(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<Feedback> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Feedback::getFeedbackUser, userByToken.getId()).orderByDesc(Feedback::getFeedbackTime);
            IPage<Feedback> iPage = feedbackMapper.selectPage(page, queryWrapper);
            List<Feedback> feedbackList = iPage.getRecords();
            List<UserFeedbackListVO> voList = CollectionUtils.isEmpty(feedbackList) ? new ArrayList<>(0) : feedbackList.stream().map(f -> {
                UserFeedbackListVO vo = new UserFeedbackListVO();
                BeanUtils.copyProperties(f, vo);
                return vo;
            }).collect(Collectors.toList());
            pageBean.setPageNum(iPage.getCurrent());
            pageBean.setPageSize(iPage.getSize());
            pageBean.setPages(iPage.getPages());
            pageBean.setTotal(iPage.getTotal());
            pageBean.setData(voList);
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("获取用户反馈列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户反馈列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    @Transactional
    public void register(UserRegirsterDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            String password = aesUtil.aesDecrypt(dto.getPassword());
            if (password.length() > 16) {
                result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",用户密码最大16个字符");
                return;
            }
            String sysCode = redisUtil.getString(redisUtil.getCacheKey(CachePrefixContent.PUBLIC_EMAIL_CODE, dto.getEmail()));

            if (StringUtils.hasLength(sysCode) && sysCode.equals(dto.getCode())) {
                // 清楚这个验证码
                redisUtil.removeKey(redisUtil.getCacheKey(CachePrefixContent.PUBLIC_EMAIL_CODE, dto.getEmail()));
                LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(User::getEmail, dto.getEmail());
                Integer count = userMapper.selectCount(queryWrapper);
                if (count >= 3) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",当前邮箱绑定用户已上限");
                } else {
                    LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    userLambdaQueryWrapper.eq(User::getUserName, dto.getUserName().trim());
                    Integer integer = userMapper.selectCount(userLambdaQueryWrapper);
                    if (integer > 0) {
                        result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",用户名已存在");
                    } else {
                        User user = new User();
                        BeanUtils.copyProperties(dto, user);
                        user.setCreateTime(LocalDateTime.now());
                        user.setUserId(IdUtil.simpleUUID());
                        user.setPassword(passwordEncoder.encode(password));
                        int insert = userMapper.insert(user);
                        if (insert == 1) {
                            // 分配角色
                            UserRole userRole = new UserRole();
                            userRole.setUserId(user.getUserId());
                            userRole.setRoleId(systemInnerConfig.getUserRoleId());
                            if (userRoleService.save(userRole)) {
                                UserSetting setting = new UserSetting();
                                setting.setUserId(user.getUserId());
                                UserAuth userAuth = new UserAuth();
                                userAuth.setUserId(user.getUserId());
                                // 创建用户设置
                                int settingFlag = userSettingMapper.insert(setting);
                                // 创建用户权限
                                int authFlag = userAuthMapper.insert(userAuth);
                                if (settingFlag == 1 && authFlag == 1) {
                                    result.setMsg("用户创建成功");
                                } else {
                                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                }
                            } else {
                                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            }
                        }
                        else {
                            result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                            result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                        }
                    }
                }
            } else {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",验证码校验失败");
            }
        } catch (Exception e) {
            log.error("新用户注册失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【新用户注册接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void operaReceiveEmailNoticeSetting(UserReceiveEmailNoticeSettingDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            UserSetting userSetting = new UserSetting();
            userSetting.setUserId(userByToken.getId());
            userSetting.setReceiveEmailNotice(dto.getReceiveEmailNotice());
            int update = userSettingMapper.updateById(userSetting);
            if (update != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
            }
        } catch (Exception e) {
            log.error("更新用户邮件接收功能失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【更新用户邮件接收功能接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void checkEmailBindStatus(CheckEmailBindDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getEmail, dto.getEmail());
            Integer integer = userMapper.selectCount(queryWrapper);
            if (integer >= 3) {
                result.setData(Boolean.FALSE);
            } else {
                result.setData(Boolean.TRUE);
            }
        } catch (Exception e) {
            log.error("验证邮箱是否绑定用户超过3个失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【验证邮箱是否绑定用户超过3个接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    @Override
    public void getPointTradeFlow(BaseRequest dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            BasePageBean pageBean = new BasePageBean();
            Page page = new Page(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<PointTradeFlow> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PointTradeFlow::getUserId, userByToken.getId()).orderByDesc(PointTradeFlow::getTradeTime);
            IPage<PointTradeFlow> iPage = pointTradeFlowMapper.selectPage(page, queryWrapper);
            List<PointTradeFlow> pointTradeFlows = iPage.getRecords();
            List<PointTradeFlowVO> voList = CollectionUtils.isEmpty(pointTradeFlows) ? new ArrayList<>(0) : pointTradeFlows.stream().map(f -> {
                PointTradeFlowVO vo = new PointTradeFlowVO();
                BeanUtils.copyProperties(f, vo);
                return vo;
            }).collect(Collectors.toList());
            pageBean.setPageNum(iPage.getCurrent());
            pageBean.setPageSize(iPage.getSize());
            pageBean.setPages(iPage.getPages());
            pageBean.setTotal(iPage.getTotal());
            pageBean.setData(voList);
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("获取用户积分交易流水失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户积分交易流水接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void sign(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            Object value = redisUtil.getValue(redisUtil.getCacheKey(CachePrefixContent.USER_EVERY_SIGN_PREFIX, userByToken.getId()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (ObjectUtils.isEmpty(value)) {
                // 给用户加积分
                Integer res = userMapper.updateOperaPointById(userByToken.getId(), 1, CommonContent.POINT_INCR);
                if (res > 0) {
                    // 记录日志
                    PointTradeFlow tradeFlow = new PointTradeFlow();
                    tradeFlow.setUserId(userByToken.getId());
                    tradeFlow.setPoint(1);
                    tradeFlow.setTradeTime(LocalDateTime.now());
                    tradeFlow.setPointType(CommonContent.POINT_INCR);
                    tradeFlow.setTradeDesc("签到送积分");
                    pointTradeFlowMapper.insert(tradeFlow);
                    // 写redis
                    LocalDateTime tomorrow = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIN);
                    LocalDateTime now = LocalDateTime.now();
                    Duration between = Duration.between(now, tomorrow);
                    redisUtil.set(redisUtil.getCacheKey(CachePrefixContent.USER_EVERY_SIGN_PREFIX, userByToken.getId()), formatter.format(now), between.getSeconds(), TimeUnit.SECONDS);
                } else {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                }
            } else {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该用户今日已签到");
            }
        } catch (Exception e) {
            log.error("用户签到失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【用户签到接口】【{}ms】 \n入参:{}\n出参:{}", "", endTime - startTime, "", result);
        }
    }

    @Override
    public void getFeedbackInfo(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            Feedback feedback = feedbackMapper.selectById(id);
            if (ObjectUtils.isEmpty(feedback) || !userByToken.getId().equals(feedback.getFeedbackUser())) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",反馈不存在");
            } else {
                UserFeedbackInfoVO vo = new UserFeedbackInfoVO();
                BeanUtils.copyProperties(feedback, vo);
                String feedbackImages = feedback.getFeedbackImages();
                if (StringUtils.hasLength(feedbackImages)) {
                    String[] split = feedbackImages.split(",");
                    vo.setFeedbackImages(Arrays.asList(split));
                }
                Integer feedbackStatus = feedback.getFeedbackStatus();
                if (feedbackStatus == 1) {
                    LambdaQueryWrapper<FeedbackReply> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(FeedbackReply::getFeedbackId, feedback.getFeedbackId());
                    FeedbackReply feedbackReply = feedbackReplyMapper.selectOne(wrapper);
                    if (!ObjectUtils.isEmpty(feedbackReply)) {
                        BeanUtils.copyProperties(feedbackReply, vo);
                    }
                }
                vo.setFeedbackContent(vo.getFeedbackContent().replace("\n", "<br/>"));
                result.setData(vo);
            }
        } catch (Exception e) {
            log.error("获取用户反馈详情失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户反馈详情接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, id, result);
        }
    }

    @Override
    public void getReportList(BaseRequest dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            BasePageBean pageBean = new BasePageBean();
            Page page = new Page(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<Report> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Report::getReportUserId, userByToken.getId()).orderByDesc(Report::getReportTime);
            IPage<Report> iPage = reportMapper.selectPage(page, queryWrapper);
            List<Report> feedbackList = iPage.getRecords();
            List<UserReportListVO> voList = CollectionUtils.isEmpty(feedbackList) ? new ArrayList<>(0) : feedbackList.stream().map(f -> {
                UserReportListVO vo = new UserReportListVO();
                BeanUtils.copyProperties(f, vo);
                return vo;
            }).collect(Collectors.toList());
            pageBean.setPageNum(iPage.getCurrent());
            pageBean.setPageSize(iPage.getSize());
            pageBean.setPages(iPage.getPages());
            pageBean.setTotal(iPage.getTotal());
            pageBean.setData(voList);
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("获取用户举报列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户举报列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    @Override
    public void getReportInfo(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            UserSubject userByToken = getUserByToken(request);
            Report report = reportMapper.selectById(id);
            if (ObjectUtils.isEmpty(report) || !userByToken.getId().equals(report.getReportUserId())) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",举报不存在");
            } else {
                UserReportInfoVO vo = new UserReportInfoVO();
                BeanUtils.copyProperties(report, vo);
                String reportImages = report.getReportImages();
                if (StringUtils.hasLength(reportImages)) {
                    String[] split = reportImages.split(",");
                    vo.setReportImages(Arrays.asList(split));
                }
                Integer reportTarget = report.getReportTarget();
                Integer reportTargetId = report.getReportTargetId();
                if (reportTarget == 1) {
                    Article article = articleMapper.selectById(reportTargetId);
                    UserReportArticleInfoVO articleInfoVO = new UserReportArticleInfoVO();
                    BeanUtils.copyProperties(article, articleInfoVO);
                    vo.setReportTargetObj(JSON.parseObject(JSON.toJSONString(articleInfoVO)));
                } else if (reportTargetId == 2) {
                    ArticleComment article = articleCommentMapper.selectById(reportTargetId);
                    UserReportCommentInfoVO commentInfoVO = new UserReportCommentInfoVO();
                    BeanUtils.copyProperties(article, commentInfoVO);
                    vo.setReportTargetObj(JSON.parseObject(JSON.toJSONString(commentInfoVO)));
                }
                result.setData(vo);
            }
        } catch (Exception e) {
            log.error("获取用户举报详情失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户举报详情接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, id, result);
        }
    }

    @Override
    public void adminGetSimpleUserList(GetSimpleUserListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            String keyWord = dto.getKeyWord();
            wrapper.orderByAsc(User::getCreateTime);
            if (StringUtils.hasLength(keyWord)) {
                wrapper.like(User::getUserName, keyWord)
                        .or().like(User::getNickName, keyWord)
                        .or().like(User::getPhone, keyWord)
                        .or().like(User::getEmail, keyWord);
            }
            wrapper.select(User::getUserId, User::getUserName, User::getNickName, User::getAvatar);
            List<User> userList = userMapper.selectList(wrapper);
            List<AdminUserViewVO> voList = CollectionUtils.isEmpty(userList) ? new ArrayList<>(0) : userList.stream().map(r -> {
                AdminUserViewVO vo = new AdminUserViewVO();
                BeanUtils.copyProperties(r, vo);
                return vo;
            }).collect(Collectors.toList());
            result.setData(voList);
        } catch (Exception e) {
            log.error("获取用户列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, dto, result);
        }
    }

    @Override
    public void getUserOrderList(BaseRequest dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            LambdaQueryWrapper<PointRechargeType> pointRechargeTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            pointRechargeTypeLambdaQueryWrapper.eq(PointRechargeType::getShows, Boolean.TRUE);
            List<PointRechargeType> pointRechargeTypes = pointRechargeTypeMapper.selectList(pointRechargeTypeLambdaQueryWrapper);
            Map<Integer, String> typeMap = CollectionUtils.isEmpty(pointRechargeTypes) ? new HashMap<>() : pointRechargeTypes.stream().collect(Collectors.toMap(PointRechargeType::getId, PointRechargeType::getTypeDesc, (o1, o2) -> o1));
            UserSubject userByToken = getUserByToken(request);
            BasePageBean pageBean = new BasePageBean();
            Page page = new Page(dto.getPageNum(), dto.getPageSize());
            LambdaQueryWrapper<PointTradeOrder> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PointTradeOrder::getOrderUser, userByToken.getId()).orderByDesc(PointTradeOrder::getOrderTime);
            IPage<PointTradeOrder> iPage = pointTradeOrderMapper.selectPage(page, queryWrapper);
            List<PointTradeOrder> feedbackList = iPage.getRecords();
            List<UserOrderListVO> voList = CollectionUtils.isEmpty(feedbackList) ? new ArrayList<>(0) : feedbackList.stream().map(f -> {
                UserOrderListVO vo = new UserOrderListVO();
                BeanUtils.copyProperties(f, vo);
                vo.setOrderDesc(typeMap.get(f.getPointTypeId()));
                return vo;
            }).collect(Collectors.toList());
            pageBean.setPageNum(iPage.getCurrent());
            pageBean.setPageSize(iPage.getSize());
            pageBean.setPages(iPage.getPages());
            pageBean.setTotal(iPage.getTotal());
            pageBean.setData(voList);
            result.setData(pageBean);
        } catch (Exception e) {
            log.error("获取用户订单列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取用户订单列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, JSON.toJSONString(dto), result);
        }
    }

    /***
     * 获取客户端IP地址;这里通过了Nginx获取;X-Real-IP
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
            if (StringUtils.hasLength(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个IP值，第一个为真实IP。
                int index = ip.indexOf(',');
                if (index != -1) {
                    ip =  ip.substring(0, index);
                }
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if("0:0:0:0:0:0:0:1".equals(ip)){
            return "127.0.0.1";
        }else {
            if(ip.equals("127.0.0.1") || ip.equalsIgnoreCase("localhost") && !StringUtils.hasLength(request.getRemoteAddr())){
                ip = request.getRemoteAddr();
            }
        }
        log.info("当前ip:" + ip);
        return ip;
    }


}




