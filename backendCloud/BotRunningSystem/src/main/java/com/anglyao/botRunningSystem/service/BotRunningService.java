package com.anglyao.botRunningSystem.service;

import org.springframework.stereotype.Service;


public interface BotRunningService {
    String addBot(Integer userId, String botCode, String input);
}
