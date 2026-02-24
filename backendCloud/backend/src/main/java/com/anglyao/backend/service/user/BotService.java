package com.anglyao.backend.service.user;

import com.anglyao.backend.pojo.Bot;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BotService {
    Map<String, String> addBot(Map<String, String> data);
    List<Bot> getBots();
    Map<String, String> removeBot(Map<String, String> data);
    Map<String, String> updateBot(Map<String, String> data);
}
