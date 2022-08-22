package com.kenzie.unit.four.ticketsystem.service;

import com.kenzie.unit.four.ticketsystem.repositories.model.ReserveTicketRecord;
import com.kenzie.unit.four.ticketsystem.service.model.ReservedTicket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CloseReservationTask implements Runnable {

    private final Integer durationToPay;
    private final ConcurrentLinkedQueue<ReservedTicket> reservedTicketsQueue;
    private final ReservedTicketService reservedTicketService;

    public CloseReservationTask(Integer durationToPay,
                                ReservedTicketService reservedTicketService,
                                ConcurrentLinkedQueue<ReservedTicket> reservedTicketsQueue) {
        this.durationToPay = durationToPay;
        this.reservedTicketService = reservedTicketService;
        this.reservedTicketsQueue = reservedTicketsQueue;
    }

    @Override
    public void run() {
       // Your code here
        ReservedTicket reservedTicket = reservedTicketService.findByReserveTicketId(reservedTicketsQueue.poll().getTicketId());
        LocalDateTime timeOfReservation = LocalDateTime.parse(reservedTicket.getDateOfReservation());
        LocalDateTime timeNow = LocalDateTime.now();
        Duration timeLapsed = Duration.between(timeOfReservation, timeNow);

        if (!reservedTicket.getTicketPurchased() && timeLapsed.getSeconds() > durationToPay) {
            ReservedTicket closedTicket = new ReservedTicket(reservedTicket.getConcertId(),
                    reservedTicket.getTicketId(),
                    reservedTicket.getDateOfReservation(), true, timeNow.toString(), false);
            reservedTicketService.updateReserveTicket(closedTicket);
        } else {
            reservedTicketsQueue.add(reservedTicket);
        }
    }
}
