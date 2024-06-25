package com.cxs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxs.auth.AuthUser;
import com.cxs.mapper.RoleMapper;
import com.cxs.mapper.UserMapper;
import com.cxs.model.Role;
import com.cxs.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StringUtils.hasLength(username)) {
            throw new UsernameNotFoundException("用户名岂能为空!");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);
        User user = userMapper.selectOne(queryWrapper);

        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        if (user.getUserStatus().equals(2)) {
            throw new LockedException("账户已被锁定,认证失败");
        }

        // 查询用户角色
        List<Role> roleList = roleMapper.selectRoleByUserId(user.getUserId());
        Collection<GrantedAuthority> grantedAuthorities = CollectionUtils.isEmpty(roleList) ? new ArrayList<>(0) :
                roleList.stream().map(r -> {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + r.getRoleName());
                    return grantedAuthority;
                }).collect(Collectors.toList());
        return new AuthUser(user.getUserId(), username, user.getPassword(), grantedAuthorities);
    }
}
