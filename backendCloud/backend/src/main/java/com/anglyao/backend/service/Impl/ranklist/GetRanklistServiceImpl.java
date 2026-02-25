package com.anglyao.backend.service.Impl.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.anglyao.backend.mapper.UserMapper;
import com.anglyao.backend.pojo.User;
import com.anglyao.backend.service.ranklist.GetRanklistService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GetRanklistServiceImpl implements GetRanklistService {
    @Resource
    private UserMapper userMapper;
    @Override
    public JSONObject getList(Integer pageID) {
        IPage<User> userIPage = new Page<>(pageID, 10);
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.orderByDesc("rating");
        List<User> users = userMapper.selectPage(userIPage, queryWrapper).getRecords();
        JSONObject response = new JSONObject();
        for (User user : users) {
            user.setPassword(null);
        }
        response.put("users", users);
        response.put("user_count", userMapper.selectCount(null));
        return response;
    }
}
