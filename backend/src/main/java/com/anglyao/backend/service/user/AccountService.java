package com.anglyao.backend.service.user;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AccountService {
    Map<String, String> getInfo();
    Map<String, String> getToken(String username, String password);
    Map<String, String> register(String username, String password, String confirmedPassword);
}
