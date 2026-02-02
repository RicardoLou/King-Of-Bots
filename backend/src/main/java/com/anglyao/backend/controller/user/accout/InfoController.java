package com.anglyao.backend.controller.user.accout;

import com.anglyao.backend.service.user.accout.InfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class InfoController {

    @Resource
    private InfoService infoService;

    @GetMapping("/account/info")
    public Map<String, String> getInfo() {
        return infoService.getInfo();
    }
}
