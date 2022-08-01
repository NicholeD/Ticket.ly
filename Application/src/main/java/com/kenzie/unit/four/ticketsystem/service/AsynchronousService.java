package com.kenzie.unit.four.ticketsystem.service;

import com.kenzie.unit.four.ticketsystem.service.model.ReservedTicket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class AsynchronousService {
    @Value("${time.to.purchase.ticket}")
    private Integer durationToPay;

    @Autowired
    private TaskExecutor executorService;

    @Autowired
    private ApplicationContext applicationContext;

    public void executeAsynchronously() {
        ReservedTicketService reservedTicketService = applicationContext.getBean(ReservedTicketService.class);
        ConcurrentLinkedQueue<ReservedTicket> queue = applicationContext.getBean(ConcurrentLinkedQueue.class);
        executorService.execute(new CloseReservationTask(durationToPay, reservedTicketService, queue));
    }
}
