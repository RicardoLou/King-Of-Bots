package com.anglyao.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pk/")
public class BotInfoController {

    @RequestMapping("getBotInfo")
    public String getBotInfo(Integer bot_id){
        return "getBotInfo";
    }
}
