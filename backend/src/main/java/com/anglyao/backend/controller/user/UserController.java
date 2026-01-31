package com.anglyao.backend.controller.user;

import com.anglyao.backend.mapper.UserMapper;
import com.anglyao.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
