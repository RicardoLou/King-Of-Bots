package com.anglyao.backend.controller.user.accout;

import com.anglyao.backend.service.user.accout.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user/account")
public class LoginController {
    @Resource
    private LoginService loginService;

    @PostMapping("/token")
    public Map<String, String> getToken(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        System.out.println(username + " " + password);
        return loginService.getToken(username, password);
    }
}
