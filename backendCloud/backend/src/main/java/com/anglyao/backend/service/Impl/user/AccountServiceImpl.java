package com.anglyao.backend.service.Impl.user;

import com.anglyao.backend.mapper.UserMapper;
import com.anglyao.backend.pojo.User;
import com.anglyao.backend.service.Impl.utils.UserDetailImpl;
import com.anglyao.backend.service.user.AccountService;
import com.anglyao.backend.utils.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {
    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> getInfo() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailImpl loginUser = (UserDetailImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("avatar", user.getAvatar());
        return map;
    }

    @Override
    public Map<String, String> getToken(String username, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetailImpl loginUser = (UserDetailImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();

        String jwt = JwtUtil.createJWT(user.getId().toString());
        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("token", jwt);

        return map;
    }


    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> map = new HashMap<>();
        username = username.trim();
        password = password.trim();
        confirmedPassword = confirmedPassword.trim();
        if (username.isEmpty() || password.isEmpty() || confirmedPassword.isEmpty()) {
            map.put("error_message", "用户名或密码不能为空");
            return map;
        }
        if (!password.equals(confirmedPassword)) {
            map.put("error_message", "两次输入的密码不一致");
            return map;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> users = userMapper.selectList(queryWrapper.eq("username", username));
        if (!users.isEmpty()) {
            map.put("error_message", "用户名已存在");
            return map;
        }
        String encoded = passwordEncoder.encode(password);
        userMapper.insert(new User(
                null,
                username,
                encoded,
                "https://big-event-ricardo.oss-cn-hangzhou.aliyuncs.com/001.jpg",
                1500));
        map.put("error_message", "success");
        return map;
    }
}
