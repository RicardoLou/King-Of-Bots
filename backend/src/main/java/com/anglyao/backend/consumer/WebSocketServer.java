package com.anglyao.backend.consumer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.anglyao.backend.mapper.UserMapper;
import com.anglyao.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    private static final ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    private static final CopyOnWriteArrayList<User> matchPool = new CopyOnWriteArrayList<>();
    private User user;
    private Session session = null;

    private static UserMapper userMapper;


    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        Integer userId = JWTAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        if (user != null) {
            users.put(userId, this);
            System.out.println("connected");
            System.out.println(users);
        } else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected");
        if (this.user != null) {
            users.remove(this.user.getId());
        }
    }

    public void start_match() {
        System.out.println("start match");
        matchPool.add(this.user);
        if (matchPool.size() >= 2) {
            Iterator<User> iterator = matchPool.iterator();
            User a = iterator.next(), b = iterator.next();
            matchPool.remove(a);
            matchPool.remove(b);

            Game game = new Game(13, 14, 10);
            game.createMap();

            // 给 1P 发送信息
            JSONObject respA = new JSONObject();
            respA.put("event", "matchSuccess");
            respA.put("opponent_username", b.getUsername());
            respA.put("opponent_avatar", b.getAvatar());
            respA.put("gameMap", game.getG());
            users.get(a.getId()).sendMessage(respA.toJSONString());

            // 给 2P 发送信息
            JSONObject respB = new JSONObject();
            respB.put("event", "matchSuccess");
            respB.put("opponent_username", a.getUsername());
            respB.put("opponent_avatar", a.getAvatar());
            respB.put("gameMap", game.getG());
            users.get(b.getId()).sendMessage(respB.toJSONString());
            System.out.println("match success");
        }
    }
    public void stop_match() {
        System.out.println("stop match");
        matchPool.remove(this.user);
    }
    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message");
        JSONObject jsonObject = JSONObject.parseObject(message);
        String event = jsonObject.getString("event");
        if ("start_match".equals(event)) {
            start_match();
        } else if ("stop_match".equals(event)) {
            stop_match();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    public void sendMessage(String message) {
        synchronized ( this.session ) {
            try {
                this.session.getBasicRemote().sendText(message);
                System.out.println("send message");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}