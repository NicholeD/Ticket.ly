package com.kenzie.unit.four.ticketsystem.service;

import com.kenzie.unit.four.ticketsystem.IntegrationTest;
import com.kenzie.unit.four.ticketsystem.service.model.Concert;
import com.kenzie.unit.four.ticketsystem.service.model.ReservedTicket;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class CloseReservationTaskTest {
    @Autowired
    ConcertService concertService;

    @Autowired
    ReservedTicketService reservedTicketService;

    private final MockNeat mockNeat = MockNeat.threadLocal();


    @Test
    public void closeReservation_timeElapses() throws Exception {
        String concertId = UUID.randomUUID().toString();
        String ticketId = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String date = LocalDate.now().toString();
        Double ticketBasePrice = 90.0;
        Concert concert = new Concert(concertId, name, date, ticketBasePrice, false);
        Concert persistedConcert = concertService.addNewConcert(concert);

        ReservedTicket reservedTicket = new ReservedTicket(persistedConcert.getId(), ticketId, LocalDateTime.now().minusSeconds(2).toString());

        reservedTicketService.reserveTicket(reservedTicket);

        Thread.sleep(1000);

        ReservedTicket persistedReservedTicket = reservedTicketService.findByReserveTicketId(ticketId);

        assertThat(persistedReservedTicket.getReservationClosed()).isTrue();
        assertThat(persistedReservedTicket.getDateReservationClosed()).isNotNull();
    }

    @Test
    public void closeReservation_notClosePurchasedTicket() throws Exception {
        String concertId = UUID.randomUUID().toString();
        String ticketId = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String date = LocalDate.now().toString();
        Double ticketBasePrice = 90.0;
        Concert concert = new Concert(concertId, name, date, ticketBasePrice, false);
        Concert persistedConcert = concertService.addNewConcert(concert);

        ReservedTicket reservedTicket = new ReservedTicket(persistedConcert.getId(), ticketId, LocalDateTime.now().toString(), false, "", true);

        reservedTicketService.reserveTicket(reservedTicket);
        reservedTicketService.updateReserveTicket(reservedTicket);

        Thread.sleep(3000);

        ReservedTicket persistedReservedTicket = reservedTicketService.findByReserveTicketId(ticketId);

        assertThat(persistedReservedTicket.getReservationClosed()).isFalse();
        assertThat(persistedReservedTicket.getDateReservationClosed()).isNull();
    }
}