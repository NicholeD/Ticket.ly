package com.kenzie.unit.four.ticketsystem.controller;

import com.kenzie.unit.four.ticketsystem.IntegrationTest;
import com.kenzie.unit.four.ticketsystem.controller.model.PurchasedTicketResponse;
import com.kenzie.unit.four.ticketsystem.service.ConcertService;
import com.kenzie.unit.four.ticketsystem.service.PurchasedTicketService;
import com.kenzie.unit.four.ticketsystem.service.ReservedTicketService;
import com.kenzie.unit.four.ticketsystem.service.model.Concert;
import com.kenzie.unit.four.ticketsystem.service.model.ReservedTicket;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class PurchasedTicketControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ConcertService concertService;

    @Autowired
    ReservedTicketService reservedTicketService;

    @Autowired
    PurchasedTicketService purchasedTicketService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void createPurchaseTicket_CreateSuccessful() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String date = LocalDate.now().toString();
        Double ticketBasePrice = 90.0;
        Concert concert = new Concert(id, name, date, ticketBasePrice, false);
        Concert persistedConcert = concertService.addNewConcert(concert);

        ReservedTicket reservedTicket = new ReservedTicket(persistedConcert.getId(), randomUUID().toString(), LocalDateTime.now().toString());
        reservedTicket = reservedTicketService.reserveTicket(reservedTicket);

        Double pricePaid = 120.0;

        // WHEN
        ResultActions actions = mvc.perform(post("/purchasedtickets/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"ticketId\": \"" + reservedTicket.getTicketId() + "\"," +
                                "\"pricePaid\": " + pricePaid +
                                "}"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // THEN
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        PurchasedTicketResponse purchasedTicketResponse = mapper.readValue(responseBody, PurchasedTicketResponse.class);
        assertThat(purchasedTicketResponse.getTicketId())
                .isEqualTo(reservedTicket.getTicketId())
                .as("The purchased ticket id should match the reservation id");
        assertThat(purchasedTicketResponse.getPricePaid())
                .isEqualTo(pricePaid)
                .as("The pricePaid should match");
        assertThat(purchasedTicketResponse.getDateOfPurchase())
                .isNotEmpty()
                .as("THe purchase date should be populated");


        ReservedTicket closedTicketReservation = reservedTicketService.findByReserveTicketId(reservedTicket.getTicketId());
        assertThat(closedTicketReservation.getReservationClosed())
                .isEqualTo(true)
                .as("Reservation must be closed after the ticket has been purchased");
    }

    @Test
    public void purchaseTicket_reservation_gets_closed() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String date = LocalDate.now().toString();
        Double ticketBasePrice = 90.0;
        Concert concert = new Concert(id, name, date, ticketBasePrice, false);
        Concert persistedConcert = concertService.addNewConcert(concert);

        ReservedTicket reservedTicket = new ReservedTicket(persistedConcert.getId(), randomUUID().toString(), LocalDateTime.now().toString());
        reservedTicketService.reserveTicket(reservedTicket);

        purchasedTicketService.purchaseTicket(reservedTicket.getTicketId(), 120.0);

        // WHEN
        ReservedTicket closedTicketReservation = reservedTicketService.findByReserveTicketId(reservedTicket.getTicketId());

        // THEN
        assertThat(closedTicketReservation.getReservationClosed())
                .isEqualTo(true)
                .as("Reservation must be closed after the ticket has been purchased");
        assertThat(closedTicketReservation.getTicketPurchased())
                .isEqualTo(true)
                .as("The reservation should have marked that the ticket has been purchased");
    }

    @Test
    public void createPurchaseTicket_fails_reservation_already_closed() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String date = LocalDate.now().toString();
        Double ticketBasePrice = 90.0;
        Concert concert = new Concert(id, name, date, ticketBasePrice, false);
        Concert persistedConcert = concertService.addNewConcert(concert);

        ReservedTicket reservedTicket = new ReservedTicket(persistedConcert.getId(), randomUUID().toString(), LocalDateTime.now().toString());
        reservedTicketService.reserveTicket(reservedTicket);

        purchasedTicketService.purchaseTicket(reservedTicket.getTicketId(), 120.0);

        // WHEN
        mvc.perform(post("/purchasedtickets/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"ticketId\": \"" + reservedTicket.getTicketId() + "\"," +
                                "\"pricePaid\": " + 150.0 +
                                "}"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
        // THEN
                .andExpect(status().is4xxClientError());
    }
}