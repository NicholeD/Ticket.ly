package com.kenzie.unit.four.ticketsystem;

import com.kenzie.unit.four.ticketsystem.service.AsynchronousService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    private AsynchronousService checkAsyncService;

    @Scheduled(fixedDelay = 200)
    public void schedule() {
        checkAsyncService.executeAsynchronously();
    }

}
