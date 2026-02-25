package com.anglyao.backend.service.Impl.record;

import com.alibaba.fastjson2.JSONObject;
import com.anglyao.backend.mapper.RecordMapper;
import com.anglyao.backend.mapper.UserMapper;
import com.anglyao.backend.pojo.Record;
import com.anglyao.backend.pojo.User;
import com.anglyao.backend.service.record.GetRecordListService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class GetRecordListServiceImpl implements GetRecordListService {
    @Resource
    private RecordMapper recordMapper;
    @Resource
    private UserMapper userMapper;
    @Override
    public JSONObject getList(Integer pageID) {
        IPage<Record> recordIPage = new Page<>(pageID, 10);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<Record>();
        queryWrapper.orderByDesc("id");
        List<Record> records = recordMapper.selectPage(recordIPage, queryWrapper).getRecords();
        JSONObject result = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for (Record record : records) {
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            JSONObject item = new JSONObject();
            if ("A".equals(record.getWinner())) {
                item.put("winner", userA.getUsername());
            } else if ("B".equals(record.getWinner())) {
                item.put("winner", userB.getUsername());
            } else {
                item.put("winner", "平局");
            }
            item.put("a_username", userA.getUsername());
            item.put("a_avatar", userA.getAvatar());
            item.put("b_username", userB.getUsername());
            item.put("b_avatar", userB.getAvatar());
            item.put("record", record);
            items.add(item);
        }
        result.put("items", items);

        // 一共有多少记录
        result.put("record_count", recordMapper.selectCount(null));

        return result;
    }
}