package com.anglyao.botRunningSystem.service.impl;

import com.anglyao.botRunningSystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Configuration
public class Consumer extends Thread{
    private Bot bot;
    private static RestTemplate restTemplate;
    private static final String ReceiveBotMoveURL = "http://127.0.0.1:3000/pk/receive/bot/move/";
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    public void startTimeout(long timeout, Bot bot) {
        this.bot = bot;
        this.start();
        try {
            this.join(timeout); // 最多等待 Timeout 毫秒
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.interrupt(); // 终端当前线程
        }

    }
    private String addUID(String code, String uid) {
        int k = code.indexOf("implements com.anglyao.botRunningSystem.utils.BotInterface");
        return code.substring(0, k).trim() + uid + " " + code.substring(k);
    }
    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        BotInterface botInterface = Reflect.compile(
                "com.anglyao.botRunningSystem.utils.Bot" + uuid.toString().substring(0, 7),
                addUID(bot.getBotCode(), uuid.toString().substring(0, 7))
        ).create().get();
        Integer direction = botInterface.nextMove(bot.getInput());
        System.out.println("move:" + bot.getUserId() + " " + direction);

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        data.add("direction", direction.toString());
        restTemplate.postForObject(ReceiveBotMoveURL, data, String.class);
    }
}