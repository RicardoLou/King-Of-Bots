package com.anglyao.backend.service.user.accout;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface InfoService {
    Map<String, String> getInfo();
}
