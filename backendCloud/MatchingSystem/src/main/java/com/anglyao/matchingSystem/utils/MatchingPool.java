package com.anglyao.matchingSystem.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchingPool extends Thread{
    private static List<Player> players = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();
    private static RestTemplate restTemplate = new RestTemplate();
    private static final String startGameURL = "http://127.0.0.1:3000/pk/start/game/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }
    public void addPlayer(Integer userId, Integer rating, Integer botId) {
        lock.lock();
        try {
            players.add(new Player(userId, rating, botId, 0));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId) {
        lock.lock();
        try {
            players.removeIf(player -> player.getUserId().equals(userId));
        } finally {
            lock.unlock();
        }
    }

    public void increaseWaitingTime() {
        for (Player player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    /**
     * 返回匹配结果
     * @param a
     * @param b
     */
    public void sendResult(Player a, Player b) {
        System.out.println("send Result");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", a.getUserId().toString());
        data.add("a_bot_id", a.getBotId().toString());
        data.add("b_id", b.getUserId().toString());
        data.add("b_bot_id", b.getBotId().toString());
        restTemplate.postForObject(startGameURL, data, String.class);
    }

    /**
     * 检查匹配结果
     * @param a
     * @param b
     * @return 匹配结果
     */
    public boolean checkMatched(Player a, Player b) {
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int waitingTime = Math.max(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }



    /**
     * 尝试匹配用户
     */
    public void matchPlayers() {
        // 优先按 Rating 排序，分值接近的排在一起
        players.sort(Comparator.comparingInt(Player::getRating));

        boolean[] used = new boolean[players.size()];
        for (int i = 0; i < players.size(); i++) {
            if (used[i]) continue;
            for (int j = i + 1; j < players.size(); j++) {
                if (used[j]) continue;

                Player a = players.get(i);
                Player b = players.get(j);

                // 剪枝：如果 Rating 差值已经超过了 a 可能接受的最大极限
                // 或者仅仅是基础的分差判断，利用排序特性直接跳出
                if (b.getRating() - a.getRating() > a.getWaitingTime() * 10 &&
                        b.getRating() - a.getRating() > b.getWaitingTime() * 10) {
                    // 后面的 Rating 会更大，更不可能匹配上，直接结束内层循环
                    break;
                }

                if (checkMatched(a, b)) {
                    used[i] = used[j] = true;
                    sendResult(a, b);
                    break;
                }
            }
        }
        List<Player> newPlayers = new ArrayList<>();
        for(int i = 0; i < players.size(); i ++) {
            if (!used[i]) {
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;
    }
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increaseWaitingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
