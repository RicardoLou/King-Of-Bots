package com.anglyao.backend.service.Impl.pk;

import com.anglyao.backend.consumer.Game;
import com.anglyao.backend.consumer.WebSocketServer;
import com.anglyao.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        System.out.println("receive bot move: " + userId + " " + direction);
        if (WebSocketServer.users.get(userId) != null) {
            Game game = WebSocketServer.users.get(userId).game;
            if (game != null) {
                if (game.getPlayerA().getId().equals(userId)) {
                    game.setNextStepAIndex(direction);
                } else if (game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepBIndex(direction);
                }
            }
        }
        return "receive success";
    }
}
