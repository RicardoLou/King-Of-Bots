package com.anglyao.backend.controller.user;

import com.anglyao.backend.service.user.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user/account")
public class AccountController {
    @Resource
    private AccountService accountService;

    @GetMapping("/info")
    public Map<String, String> getInfo() {
        return accountService.getInfo();
    }
    @PostMapping("/token")
    public Map<String, String> getToken(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        System.out.println(username + " " + password);
        return accountService.getToken(username, password);
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestParam Map<String, String> map) {
        return accountService.register(map.get("username"), map.get("password"), map.get("confirmedPassword"));
    }
}
