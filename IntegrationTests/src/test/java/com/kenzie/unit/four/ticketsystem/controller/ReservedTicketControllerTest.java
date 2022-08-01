package com.kenzie.unit.four.ticketsystem.controller;

import com.kenzie.unit.four.ticketsystem.IntegrationTest;
import com.kenzie.unit.four.ticketsystem.controller.model.ReservedTicketCreateRequest;
import com.kenzie.unit.four.ticketsystem.controller.model.ReservedTicketResponse;
import com.kenzie.unit.four.ticketsystem.service.ConcertService;
import com.kenzie.unit.four.ticketsystem.service.ReservedTicketService;
import com.kenzie.unit.four.ticketsystem.service.model.Concert;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class ReservedTicketControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ConcertService concertService;

    @Autowired
    ReservedTicketService reservedTicketService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void createReserveTicket_CreateSuccessful() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String date = LocalDate.now().toString();
        Double ticketBasePrice = 90.0;
        Concert concert = new Concert(id, name, date, ticketBasePrice, false);
        Concert persistedConcert = concertService.addNewConcert(concert);

        ReservedTicketCreateRequest reservedTicketCreateRequest = new ReservedTicketCreateRequest();
        reservedTicketCreateRequest.setConcertId(persistedConcert.getId());

        mapper.registerModule(new JavaTimeModule());

        // WHEN
        ResultActions actions = mvc.perform(post("/reservedtickets")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reservedTicketCreateRequest)))
        // THEN
                .andExpect(jsonPath("ticketId")
                        .exists())
                .andExpect(jsonPath("concertId")
                        .value(is(id)))
                .andExpect(jsonPath("dateOfReservation")
                        .exists())
                .andExpect(jsonPath("purchasedTicket")
                        .value(is(false)))
                .andExpect(jsonPath("reservationClosed")
                        .value(is(false)))
                .andExpect(status().is2xxSuccessful());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        ReservedTicketResponse reservedTicketResponse = mapper.readValue(responseBody, ReservedTicketResponse.class);
        assertThat(reservedTicketService.findByReserveTicketId(reservedTicketResponse.getTicketId()))
                .isNotNull()
                .as("The reserved ticket must be saved to the database");
    }

    @Test
    public void createReserveTicket_Concert_DoesNotExist() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();

        ReservedTicketCreateRequest reservedTicketCreateRequest = new ReservedTicketCreateRequest();
        reservedTicketCreateRequest.setConcertId(id);

        mapper.registerModule(new JavaTimeModule());

        // WHEN
        mvc.perform(post("/reservedtickets")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reservedTicketCreateRequest)))
        // THEN
                .andExpect(status().isBadRequest());
    }
}