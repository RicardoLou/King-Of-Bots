package com.anglyao.backend.service.user.accout;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface RegisterService {
    Map<String, String> register(String username, String password, String confirmedPassword);
}
