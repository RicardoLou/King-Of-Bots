package com.anglyao.backend.service.Impl.user.accout;

import com.anglyao.backend.mapper.UserMapper;
import com.anglyao.backend.pojo.User;
import com.anglyao.backend.service.user.accout.RegisterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

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
        userMapper.insert(new User(null, username, encoded, "https://cdn.acwing.com/media/user/profile/photo/10905_lg_f5f0d5c0c7.jpg"));
        map.put("error_message", "success");
        return null;
    }
}
