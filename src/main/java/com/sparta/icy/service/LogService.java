package com.sparta.icy.service;

import com.sparta.icy.entity.Log;
import com.sparta.icy.repository.LogRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LogService {
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void addLog(String username, String action) {
        Log log = new Log();
        log.setUsername(username);
        log.setAction(action);
        log.setTimestamp(new Date());
        logRepository.save(log);
    }
}
