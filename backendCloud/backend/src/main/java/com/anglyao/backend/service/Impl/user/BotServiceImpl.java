package com.anglyao.backend.service.Impl.user;

import com.anglyao.backend.mapper.BotMapper;
import com.anglyao.backend.pojo.Bot;
import com.anglyao.backend.pojo.User;
import com.anglyao.backend.service.Impl.utils.UserDetailImpl;
import com.anglyao.backend.service.Impl.utils.UserDetailServiceImpl;
import com.anglyao.backend.service.user.BotService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class BotServiceImpl implements BotService {
    @Resource
    private BotMapper botMapper;

    @Override
    public Map<String, String> addBot(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailImpl loginUser = (UserDetailImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser(); // 当前登录用户

        String botName = data.get("botName");
        String description = data.get("description");
        String code = data.get("code");

        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), botName, description, code, now, now);
        Map<String, String> map = new HashMap<>();

        if (description == null || description.isEmpty()) {
            bot.setDescription("这个用户很懒，什么都没留下~");
        }

        if (botName.isEmpty() || code.isEmpty()) {
            map.put("error_message", "参数不足");
            return map;
        }
        botMapper.insert(bot);
        map.put("error_message", "success");
        return map;
    }

    @Override
    public List<Bot> getBots() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailImpl loginUser = (UserDetailImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser(); // 当前登录用户
        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator_id", user.getId());
        return botMapper.selectList(queryWrapper);
    }

    @Override
    public Map<String, String> removeBot(Map<String, String> data) {
        Map<String, String> map = new HashMap<>();

        int id = Integer.parseInt(data.get("bot_id"));
        Bot bot = botMapper.selectById(id);

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailImpl loginUser = (UserDetailImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser(); // 当前登录用户
        if (!Objects.equals(user.getId(), bot.getCreatorId())) {
            map.put("error_message", "没有权限");
            return map;
        }
        botMapper.deleteById(id);
        map.put("error_message", "success");
        return map;
    }

    @Override
    public Map<String, String> updateBot(Map<String, String> data) {
        Map<String, String> map = new HashMap<>();
        int id = Integer.parseInt(data.get("bot_id"));
        Bot bot = botMapper.selectById(id);
        if (bot == null) {
            map.put("error_message", "不存在该机器人");
            return map;
        }
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailImpl loginUser = (UserDetailImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser(); // 当前登录用户
        if (!Objects.equals(user.getId(), bot.getCreatorId())) {
            map.put("error_message", "没有权限");
            return map;
        }
        String botName = data.get("botName");
        String description = data.get("description");
        String code = data.get("code");

        if (description == null || description.isEmpty()) {
            description = "这个用户很懒，什么也没留下~";
        }

        if (botName.isEmpty() || code.isEmpty()) {
            map.put("error_message", "参数不足");
            return map;
        }

        bot.setBotName(botName);
        bot.setDescription(description);
        bot.setCode(code);
        bot.setUpdateTime(new Date());
        botMapper.updateById(bot);

        map.put("error_message", "success");
        return map;
    }
}
