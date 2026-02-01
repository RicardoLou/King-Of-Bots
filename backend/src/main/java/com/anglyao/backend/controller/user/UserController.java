package com.anglyao.backend.controller.user;

import com.anglyao.backend.mapper.UserMapper;
import com.anglyao.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("/all")
    public List<User> getUser() {
        return userMapper.selectList(null);
    }

    @GetMapping("/add/{userId}/{username}/{password}")
    public String addUser(
            @PathVariable Integer userId,
            @PathVariable String username,
            @PathVariable String password) {
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));
        userMapper.insert(user);
        return "添加成功";
    }
}
