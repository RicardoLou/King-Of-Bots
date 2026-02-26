package com.anglyao.backend.controller.record;

import com.alibaba.fastjson2.JSONObject;
import com.anglyao.backend.service.record.GetRecordListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/record")
public class GetRecordListController {
    @Resource
    private GetRecordListService getRecordListService;

    @GetMapping("/list/")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer pageID = Integer.valueOf(data.get("pageID"));
        return getRecordListService.getList(pageID);
    }
}
