package com.sparta.icy.service;

import com.sparta.icy.entity.Log;
import com.sparta.icy.repository.LogRepository;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void addLog(String username, String action) {
        Log log = new Log(username, action);
        logRepository.save(log);
    }

    public void addLoginLog(String username) {
        addLog(username, "로그인");
    }

    public void addLogoutLog(String username) {
        addLog(username, "로그아웃");
    }
}
