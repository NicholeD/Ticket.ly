package com.kenzie.unit.four.ticketsystem.service;

import com.kenzie.unit.four.ticketsystem.repositories.PurchaseTicketRepository;
import com.kenzie.unit.four.ticketsystem.repositories.model.PurchasedTicketRecord;
import com.kenzie.unit.four.ticketsystem.service.model.PurchasedTicket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PurchasedTicketServiceTest {
    private PurchaseTicketRepository purchaseTicketRepository;
    private ReservedTicketService reservedTicketService;
    private PurchasedTicketService purchasedTicketService;

    @BeforeEach
    void setup() {
        purchaseTicketRepository = mock(PurchaseTicketRepository.class);
        reservedTicketService = mock(ReservedTicketService.class);
        purchasedTicketService = new PurchasedTicketService(purchaseTicketRepository, reservedTicketService);
    }

    /** ------------------------------------------------------------------------
     *  purchasedTicketService.purchaseTicket
     *  ------------------------------------------------------------------------ **/

    // Write additional tests here

    /** ------------------------------------------------------------------------
     *  purchasedTicketService.findByConcertId
     *  ------------------------------------------------------------------------ **/

    @Test
    void findByConcertId() {
        // GIVEN
        String concertId = randomUUID().toString();

        PurchasedTicketRecord record = new PurchasedTicketRecord();
        record.setConcertId(concertId);
        record.setTicketId(randomUUID().toString());
        record.setDateOfPurchase("purchasedate");
        record.setPricePaid(11.0);

        // WHEN
        when(purchaseTicketRepository.findByConcertId(concertId)).thenReturn(Arrays.asList(record));
        List<PurchasedTicket> purchasedTickets = purchasedTicketService.findByConcertId(concertId);

        // THEN
        Assertions.assertEquals(1, purchasedTickets.size(), "There is one Purchased Ticket");
        PurchasedTicket ticket = purchasedTickets.get(0);
        Assertions.assertNotNull(ticket, "The purchased ticket is returned");
        Assertions.assertEquals(record.getConcertId(), ticket.getConcertId(), "The concert id matches");
        Assertions.assertEquals(record.getTicketId(), ticket.getTicketId(), "The ticket id matches");
        Assertions.assertEquals(record.getDateOfPurchase(), ticket.getDateOfPurchase(), "The date of purchase matches");
        Assertions.assertEquals(record.getPricePaid(), ticket.getPricePaid(), "The price paid matches");
    }

    @Test
    void findByConcertId_no_purchased_tickets() {
        // GIVEN
        String concertId = randomUUID().toString();

        // WHEN
        when(purchaseTicketRepository.findByConcertId(concertId)).thenReturn(new ArrayList<PurchasedTicketRecord>());
        List<PurchasedTicket> purchasedTickets = purchasedTicketService.findByConcertId(concertId);

        // THEN
        Assertions.assertEquals(0, purchasedTickets.size(), "There are no Purchased Tickets");
    }
}
