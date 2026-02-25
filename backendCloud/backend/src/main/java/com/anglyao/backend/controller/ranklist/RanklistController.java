package com.anglyao.backend.controller.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.anglyao.backend.service.ranklist.GetRanklistService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class RanklistController {
    @Resource
    private GetRanklistService getRanklistService;

    @GetMapping("/ranklist/")
    public JSONObject getRanklist(@RequestParam Map<String, String> data) {
        Integer pageID = Integer.parseInt(data.get("pageID"));
        return getRanklistService.getList(pageID);
    }
}
