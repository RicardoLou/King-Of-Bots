package com.anglyao.botRunningSystem.controller;

import com.anglyao.botRunningSystem.service.BotRunningService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/bot")
public class BotRunningSystemController {
    @Resource
    private BotRunningService botRunningService;

    @PostMapping("/add/")
    public String addPlayer(@RequestParam MultiValueMap<String, String> data) {
        return botRunningService.addBot(
                Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id"))),
                data.getFirst("bot_code"),
                data.getFirst("input"));
    }
}
