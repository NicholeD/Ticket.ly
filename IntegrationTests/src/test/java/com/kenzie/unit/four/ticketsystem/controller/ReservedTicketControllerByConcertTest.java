package com.kenzie.unit.four.ticketsystem.controller;

import com.kenzie.unit.four.ticketsystem.IntegrationTest;
import com.kenzie.unit.four.ticketsystem.controller.model.ReservedTicketResponse;
import com.kenzie.unit.four.ticketsystem.service.ConcertService;
import com.kenzie.unit.four.ticketsystem.service.ReservedTicketService;
import com.kenzie.unit.four.ticketsystem.service.model.Concert;
import com.kenzie.unit.four.ticketsystem.service.model.ReservedTicket;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class ReservedTicketControllerByConcertTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ConcertService concertService;

    @Autowired
    ReservedTicketService reservedTicketService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getReserveTicketsByConcert_Successful() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String date = LocalDate.now().toString();
        Double ticketBasePrice = 90.0;
        Concert concert = new Concert(id, name, date, ticketBasePrice, false);
        Concert persistedConcert = concertService.addNewConcert(concert);

        ReservedTicket reservedTicket = new ReservedTicket(persistedConcert.getId(), randomUUID().toString(), LocalDateTime.now().toString());
        ReservedTicket secondReservedTicket = new ReservedTicket(persistedConcert.getId(), randomUUID().toString(), LocalDateTime.now().toString());

        reservedTicketService.reserveTicket(reservedTicket);
        reservedTicketService.reserveTicket(secondReservedTicket);

        mapper.registerModule(new JavaTimeModule());

        // WHEN
        ResultActions actions = mvc.perform(get("/reservedtickets/concerts/{concertId}", persistedConcert.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // THEN
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        List<ReservedTicketResponse> listReservedTickets = mapper.readValue(responseBody, new TypeReference<List<ReservedTicketResponse>>(){});
        assertThat(listReservedTickets.size()).isEqualTo(2);
    }

    @Test
    public void getReserveTicketsByConcert_NotFound() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String date = LocalDate.now().toString();
        Double ticketBasePrice = 90.0;
        Concert concert = new Concert(id, name, date, ticketBasePrice, false);
        Concert persistedConcert = concertService.addNewConcert(concert);

        mapper.registerModule(new JavaTimeModule());

        // WHEN
        ResultActions actions = mvc.perform(get("/reservedtickets/concerts/{concertId}", persistedConcert.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
        // THEN
                .andExpect(status().isNoContent());
    }
}