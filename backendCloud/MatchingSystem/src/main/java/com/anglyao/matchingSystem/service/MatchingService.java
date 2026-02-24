package com.anglyao.matchingSystem.service;

import org.springframework.stereotype.Service;

@Service
public interface MatchingService {
    String addPlayer(Integer userId, Integer rating);
    String removePlayer(Integer userId);
}
