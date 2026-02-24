package com.anglyao.backend.controller.pk;

import com.anglyao.backend.service.pk.StartGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/pk")
public class StartGameController {
    @Resource
    private StartGameService startGameService;

    @PostMapping("/start/game/")
    public String startGame(@RequestParam MultiValueMap<String, String>  data) {
        return startGameService.startGame(
                Integer.parseInt(Objects.requireNonNull(data.getFirst("a_id"))),
                Integer.parseInt(Objects.requireNonNull(data.getFirst("a_bot_id"))),
                Integer.parseInt(Objects.requireNonNull(data.getFirst("b_id"))),
                Integer.parseInt(Objects.requireNonNull(data.getFirst("b_bot_id")))
        );
    }
}