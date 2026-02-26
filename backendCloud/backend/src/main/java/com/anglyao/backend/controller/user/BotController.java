package com.anglyao.backend.controller.user;

import com.anglyao.backend.pojo.Bot;
import com.anglyao.backend.service.user.BotService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/bot")
public class BotController {
    @Resource
    private BotService botService;

    @PostMapping("/add")
    public Map<String, String> addBot(@RequestBody Map<String, String> data) {
        System.out.println(data);
        return botService.addBot(data);
    }

    @GetMapping("/botList")
    public List<Bot> getBots() {
        return botService.getBots();
    }

    @PostMapping("/remove")
    public Map<String, String> removeBot(@RequestBody Map<String, String> data) {
        return botService.removeBot(data);
    }

    @PostMapping("/update")
    public Map<String, String> updateBot(@RequestBody Map<String, String> data) {
        return botService.updateBot(data);
    }
}
