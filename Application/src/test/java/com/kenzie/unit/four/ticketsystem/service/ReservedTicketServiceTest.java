package com.kenzie.unit.four.ticketsystem.service;

import com.kenzie.unit.four.ticketsystem.repositories.ReservedTicketRepository;
import com.kenzie.unit.four.ticketsystem.repositories.model.ConcertRecord;
import com.kenzie.unit.four.ticketsystem.repositories.model.ReserveTicketRecord;
import com.kenzie.unit.four.ticketsystem.service.model.Concert;
import com.kenzie.unit.four.ticketsystem.service.model.ReservedTicket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReservedTicketServiceTest {

    private ReservedTicketRepository reservedTicketRepository;
    private ConcertService concertService;
    private ConcurrentLinkedQueue<ReservedTicket> reservedTicketsQueue;
    private ReservedTicketService reservedTicketService;

    @BeforeEach
    void setup() {
        reservedTicketRepository = mock(ReservedTicketRepository.class);
        concertService = mock(ConcertService.class);
        reservedTicketsQueue = new ConcurrentLinkedQueue<>();
        reservedTicketService = new ReservedTicketService(reservedTicketRepository, concertService, reservedTicketsQueue);
    }

    /** ------------------------------------------------------------------------
     *  reservedTicketService.findAllReservationTickets
     *  ------------------------------------------------------------------------ **/

    @Test
    void findAllReservationTickets() {
        // GIVEN
        ReserveTicketRecord record1 = new ReserveTicketRecord();
        record1.setTicketId(randomUUID().toString());
        record1.setConcertId(randomUUID().toString());
        record1.setDateOfReservation("record1date");
        record1.setDateReservationClosed("closed1date");
        record1.setReservationClosed(false);
        record1.setPurchasedTicket(true);

        ReserveTicketRecord record2 = new ReserveTicketRecord();
        record2.setTicketId(randomUUID().toString());
        record2.setConcertId(randomUUID().toString());
        record2.setDateOfReservation("record2date");
        record2.setDateReservationClosed("closed2date");
        record2.setReservationClosed(true);
        record2.setPurchasedTicket(false);

        List<ReserveTicketRecord> records = new ArrayList<>();

        records.add(record1);
        records.add(record2);

        when(reservedTicketRepository.findAll()).thenReturn(records);
        // WHEN

        List<ReservedTicket> reservations = reservedTicketService.findAllReservationTickets();

        // THEN
        Assertions.assertNotNull(reservations, "The reserved ticket list is returned");
        Assertions.assertEquals(2, reservations.size(), "There are two reserved tickets");

        for (ReservedTicket ticket : reservations) {
            if (ticket.getTicketId() == record1.getTicketId()) {
                Assertions.assertEquals(record1.getConcertId(), ticket.getConcertId(), "The concert id matches");
                Assertions.assertEquals(record1.getDateOfReservation(), ticket.getDateOfReservation(), "The reservation date matches");
                Assertions.assertEquals(record1.getReservationClosed(), ticket.getReservationClosed(), "The reservationClosed matches");
                Assertions.assertEquals(record1.getPurchasedTicket(), ticket.getTicketPurchased(), "The ticketPurchased matches");
                Assertions.assertEquals(record1.getDateReservationClosed(), ticket.getDateReservationClosed(), "The reservation closed date matches");
            } else if (ticket.getTicketId() == record2.getTicketId()) {
                Assertions.assertEquals(record2.getConcertId(), ticket.getConcertId(), "The concert id matches");
                Assertions.assertEquals(record2.getDateOfReservation(), ticket.getDateOfReservation(), "The reservation date matches");
                Assertions.assertEquals(record2.getReservationClosed(), ticket.getReservationClosed(), "The reservationClosed matches");
                Assertions.assertEquals(record2.getPurchasedTicket(), ticket.getTicketPurchased(), "The ticketPurchased matches");
                Assertions.assertEquals(record2.getDateReservationClosed(), ticket.getDateReservationClosed(), "The reservation closed date matches");
            } else {
                Assertions.assertTrue(false, "Reserved Ticket returned that was not in the records!");
            }
        }
    }

    /** ------------------------------------------------------------------------
     *  reservedTicketService.findAllUnclosedReservationTickets
     *  ------------------------------------------------------------------------ **/

    @Test
    void findAllUnclosedReservationTickets() {
        //GIVEN
        List<ReserveTicketRecord> reservedTicketRecords = new ArrayList<>();

        ReserveTicketRecord closedReservedRecord = new ReserveTicketRecord();
        closedReservedRecord.setConcertId("C1");
        closedReservedRecord.setTicketId("T1");
        closedReservedRecord.setDateOfReservation("11/11/21");
        closedReservedRecord.setReservationClosed(true);
        closedReservedRecord.setDateReservationClosed("11/11/21");
        closedReservedRecord.setPurchasedTicket(true);

        ReservedTicket closedTicket = new ReservedTicket(closedReservedRecord.getConcertId(),
                closedReservedRecord.getTicketId(),
                closedReservedRecord.getDateOfReservation(),
                closedReservedRecord.getReservationClosed(),
                closedReservedRecord.getDateReservationClosed(),
                closedReservedRecord.getPurchasedTicket());

        ReserveTicketRecord closedReservedRecord2 = new ReserveTicketRecord();
        closedReservedRecord.setConcertId("C1");
        closedReservedRecord.setTicketId("T3");
        closedReservedRecord.setDateOfReservation("11/11/21");
        closedReservedRecord.setReservationClosed(true);
        closedReservedRecord.setDateReservationClosed("11/11/21");
        closedReservedRecord.setPurchasedTicket(false);

        ReservedTicket closedTicket2 = new ReservedTicket(closedReservedRecord.getConcertId(),
                closedReservedRecord.getTicketId(),
                closedReservedRecord.getDateOfReservation(),
                closedReservedRecord.getReservationClosed(),
                closedReservedRecord.getDateReservationClosed(),
                closedReservedRecord.getPurchasedTicket());

        ReserveTicketRecord unclosedReservedRecord = new ReserveTicketRecord();
        unclosedReservedRecord.setConcertId("C1");
        unclosedReservedRecord.setTicketId("T2");
        unclosedReservedRecord.setDateOfReservation("11/11/21");
        unclosedReservedRecord.setReservationClosed(false);
        unclosedReservedRecord.setDateReservationClosed("11/11/21");
        unclosedReservedRecord.setPurchasedTicket(false);

        ReservedTicket unclosedTicket = new ReservedTicket(unclosedReservedRecord.getConcertId(),
                unclosedReservedRecord.getTicketId(),
                unclosedReservedRecord.getDateOfReservation(),
                unclosedReservedRecord.getReservationClosed(),
                unclosedReservedRecord.getDateReservationClosed(),
                unclosedReservedRecord.getPurchasedTicket());

        reservedTicketRecords.add(closedReservedRecord);
        reservedTicketRecords.add(unclosedReservedRecord);
        reservedTicketRecords.add(closedReservedRecord2);


        when(reservedTicketRepository.findAll()).thenReturn(reservedTicketRecords);

        //WHEN
        List<ReservedTicket> unclosedReservationTickets = reservedTicketService.findAllUnclosedReservationTickets();

        //THEN
        Assertions.assertEquals(unclosedReservationTickets.get(0).getTicketId(), unclosedTicket.getTicketId());
        Assertions.assertFalse(unclosedReservationTickets.contains(closedTicket));
        Assertions.assertFalse(unclosedReservationTickets.contains(closedTicket2));
    }

    /** ------------------------------------------------------------------------
     *  reservedTicketService.reserveTicket
     *  ------------------------------------------------------------------------ **/

    @Test
    void reserveTicket() {
        //GIVEN
        ReservedTicket reservedTicket = new ReservedTicket(randomUUID().toString(),
                randomUUID().toString(),
                "reserved date",
                false,
                null,
                false);

        Concert concert = new Concert(reservedTicket.getConcertId(),
                "Yo-yo Ma",
                "11/11/2023",
                105.00,
                false);

        when(concertService.findByConcertId(any())).thenReturn(concert);

        //WHEN
        ReservedTicket reservedTicket2 = reservedTicketService.reserveTicket(reservedTicket);

        //THEN
        Assertions.assertEquals(reservedTicket, reservedTicket2);
    }

    @Test
    void reserveTicket_withNullConcert_throwsResponseStatusException() {
        //GIVEN
        ReservedTicket reservedTicket = new ReservedTicket(randomUUID().toString(),
                randomUUID().toString(),
                "reserved date");

        when(concertService.findByConcertId(any(String.class))).thenReturn(null);

        //THEN - WHEN
        Assertions.assertThrows(ResponseStatusException.class,
                ()-> reservedTicketService.reserveTicket(reservedTicket));

    }

    @Test
    void reserveTicket_concertReservationClosed_throwsResponseStatusException() {
        //GIVEN
        ReservedTicket reservedTicket = new ReservedTicket(randomUUID().toString(),
                randomUUID().toString(),
                "reserved date");

        Concert concert = new Concert(reservedTicket.getConcertId(),
                "Yo-yo Ma",
                "11/11/2023",
                105.00,
                true);

        when(concertService.findByConcertId(any(String.class))).thenReturn(concert);

        //THEN - WHEN
        Assertions.assertThrows(ResponseStatusException.class,
                ()-> reservedTicketService.reserveTicket(reservedTicket));
    }

    /** ------------------------------------------------------------------------
     *  reservedTicketService.findByReserveTicketId
     *  ------------------------------------------------------------------------ **/

    @Test
    void findByReserveTicketId() {
        // GIVEN
        ReserveTicketRecord record = new ReserveTicketRecord();
        record.setTicketId(randomUUID().toString());
        record.setConcertId(randomUUID().toString());
        record.setDateOfReservation("record2date");
        record.setDateReservationClosed("closed2date");
        record.setReservationClosed(true);
        record.setPurchasedTicket(false);

        when(reservedTicketRepository.findById(record.getTicketId())).thenReturn(Optional.of(record));

        // WHEN
        ReservedTicket reservedTicket = reservedTicketService.findByReserveTicketId(record.getTicketId());

        // THEN
        Assertions.assertNotNull(reservedTicket);
        Assertions.assertEquals(record.getConcertId(), reservedTicket.getConcertId(), "The concert id matches");
        Assertions.assertEquals(record.getDateOfReservation(), reservedTicket.getDateOfReservation(), "The reservation date matches");
        Assertions.assertEquals(record.getReservationClosed(), reservedTicket.getReservationClosed(), "The reservationClosed matches");
        Assertions.assertEquals(record.getPurchasedTicket(), reservedTicket.getTicketPurchased(), "The ticketPurchased matches");
        Assertions.assertEquals(record.getDateReservationClosed(), reservedTicket.getDateReservationClosed(), "The reservation closed date matches");
    }


    /** ------------------------------------------------------------------------
     *  reservedTicketService.findByConcertId
     *  ------------------------------------------------------------------------ **/

    // Write additional tests here
    @Test
    void findByConcertId() {
        //GIVEN
        List<ReserveTicketRecord> reservedRecords = new ArrayList<>();
        ReserveTicketRecord reserveTicketRecord = new ReserveTicketRecord();
        reserveTicketRecord.setConcertId("abc");
        reserveTicketRecord.setTicketId("123");
        reserveTicketRecord.setDateOfReservation("08/08/23");
        reserveTicketRecord.setReservationClosed(false);
        reserveTicketRecord.setDateReservationClosed("12/08/22");
        reserveTicketRecord.setPurchasedTicket(true);
        reservedRecords.add(reserveTicketRecord);

        ReservedTicket ticket = new ReservedTicket(reserveTicketRecord.getConcertId(),
                reserveTicketRecord.getTicketId(),
                reserveTicketRecord.getDateOfReservation(),
                reserveTicketRecord.getReservationClosed(),
                reserveTicketRecord.getDateReservationClosed(),
                reserveTicketRecord.getPurchasedTicket());

        when(reservedTicketRepository.findByConcertId(any())).thenReturn(reservedRecords);

        //WHEN
        List<ReservedTicket> reservedTickets = reservedTicketService.findByConcertId("abc");

        //THEN
        Assertions.assertEquals(reservedTickets.get(0).getTicketId(), ticket.getTicketId());
        Assertions.assertEquals(reservedTickets.get(0).getConcertId(), ticket.getConcertId());
        Assertions.assertEquals(reservedTickets.get(0).getDateOfReservation(), ticket.getDateOfReservation());
        Assertions.assertEquals(reservedTickets.get(0).getReservationClosed(), ticket.getReservationClosed());
        Assertions.assertEquals(reservedTickets.get(0).getDateReservationClosed(), ticket.getDateReservationClosed());

    }

    /** ------------------------------------------------------------------------
     *  reservedTicketService.updateReserveTicket
     *  ------------------------------------------------------------------------ **/

    @Test
    void updateReserveTicket() {
        // GIVEN
        ReserveTicketRecord record = new ReserveTicketRecord();
        record.setTicketId(randomUUID().toString());
        record.setConcertId(randomUUID().toString());
        record.setDateOfReservation("record2date");
        record.setDateReservationClosed("closed2date");
        record.setReservationClosed(true);
        record.setPurchasedTicket(false);

        ReservedTicket reservedTicket = new ReservedTicket(
                record.getConcertId(),
                record.getTicketId(),
                record.getDateOfReservation(),
                record.getReservationClosed(),
                record.getDateReservationClosed(),
                record.getPurchasedTicket());

        ArgumentCaptor<ReserveTicketRecord> recordCaptor = ArgumentCaptor.forClass(ReserveTicketRecord.class);

        // WHEN
        reservedTicketService.updateReserveTicket(reservedTicket);

        // THEN
        verify(reservedTicketRepository).save(recordCaptor.capture());
        ReserveTicketRecord storedRecord = recordCaptor.getValue();

        Assertions.assertNotNull(reservedTicket);
        Assertions.assertEquals(storedRecord.getConcertId(), reservedTicket.getConcertId(), "The concert id matches");
        Assertions.assertEquals(storedRecord.getDateOfReservation(), reservedTicket.getDateOfReservation(), "The reservation date matches");
        Assertions.assertEquals(storedRecord.getReservationClosed(), reservedTicket.getReservationClosed(), "The reservationClosed matches");
        Assertions.assertEquals(storedRecord.getPurchasedTicket(), reservedTicket.getTicketPurchased(), "The ticketPurchased matches");
        Assertions.assertEquals(storedRecord.getDateReservationClosed(), reservedTicket.getDateReservationClosed(), "The reservation closed date matches");
    }
}
