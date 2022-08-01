package com.kenzie.unit.four.ticketsystem.controller;

import com.kenzie.unit.four.ticketsystem.IntegrationTest;
import com.kenzie.unit.four.ticketsystem.controller.model.ConcertCreateRequest;
import com.kenzie.unit.four.ticketsystem.controller.model.ConcertUpdateRequest;
import com.kenzie.unit.four.ticketsystem.service.ConcertService;
import com.kenzie.unit.four.ticketsystem.service.model.Concert;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class ConcertControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ConcertService concertService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getConcert_ConcertExists() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String date = LocalDate.now().toString();
        Double ticketBasePrice = 80.0;

        Concert concert = new Concert(id, name, date, ticketBasePrice, false);
        Concert persistedConcert = concertService.addNewConcert(concert);

        // WHEN
        mvc.perform(get("/concerts/{concertId}", persistedConcert.getId())
                        .accept(MediaType.APPLICATION_JSON))
        // THEN
                .andExpect(jsonPath("id")
                        .value(is(id)))
                .andExpect(jsonPath("name")
                        .value(is(name)))
                .andExpect(jsonPath("date")
                        .value(is(date)))
                .andExpect(jsonPath("ticketBasePrice")
                        .value(is(ticketBasePrice)))
                .andExpect(jsonPath("reservationClosed")
                        .value(is(false)))
                .andExpect(status().isOk());
    }

    @Test
    public void getConcert_ConcertDoesNotExist() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        // WHEN
        mvc.perform(get("/concerts/{concertId}", id)
                        .accept(MediaType.APPLICATION_JSON))
        // THEN
                .andExpect(status().isNotFound());
    }

    @Test
    public void createConcert_CreateSuccessful() throws Exception {
        // GIVEN
        String name = mockNeat.strings().valStr();
        String date = LocalDate.now().toString();
        Double ticketBasePrice = 90.0;

        ConcertCreateRequest concertCreateRequest = new ConcertCreateRequest();
        concertCreateRequest.setDate(LocalDate.now());
        concertCreateRequest.setName(name);
        concertCreateRequest.setTicketBasePrice(ticketBasePrice);

        mapper.registerModule(new JavaTimeModule());

        // WHEN
        mvc.perform(post("/concerts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(concertCreateRequest)))
        // THEN
                .andExpect(jsonPath("id")
                        .exists())
                .andExpect(jsonPath("name")
                        .value(is(name)))
                .andExpect(jsonPath("date")
                        .value(is(date)))
                .andExpect(jsonPath("ticketBasePrice")
                        .value(is(ticketBasePrice)))
                .andExpect(jsonPath("reservationClosed")
                        .value(is(false)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateConcert_PutSuccessful() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String date = LocalDate.now().toString();
        Double ticketBasePrice = 90.0;

        Concert concert = new Concert(id, name, date, ticketBasePrice, false);
        Concert persistedConcert = concertService.addNewConcert(concert);

        String newName = mockNeat.strings().valStr();
        Double newTicketBasePrice = 100.0;

        ConcertUpdateRequest concertUpdateRequest = new ConcertUpdateRequest();
        concertUpdateRequest.setId(id);
        concertUpdateRequest.setDate(LocalDate.now());
        concertUpdateRequest.setName(newName);
        concertUpdateRequest.setTicketBasePrice(newTicketBasePrice);
        concertUpdateRequest.setReservationClosed(true);

        mapper.registerModule(new JavaTimeModule());

        // WHEN
        mvc.perform(put("/concerts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(concertUpdateRequest)))
        // THEN
                .andExpect(jsonPath("id")
                        .exists())
                .andExpect(jsonPath("name")
                        .value(is(newName)))
                .andExpect(jsonPath("date")
                        .value(is(date)))
                .andExpect(jsonPath("ticketBasePrice")
                        .value(is(newTicketBasePrice)))
                .andExpect(jsonPath("reservationClosed")
                        .value(is(true)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteConcert_DeleteSuccessful() throws Exception {
        // GIVEN
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String date = LocalDate.now().toString();
        Double ticketBasePrice = 90.0;

        Concert concert = new Concert(id, name, date, ticketBasePrice, false);
        Concert persistedConcert = concertService.addNewConcert(concert);

        // WHEN
        mvc.perform(delete("/concerts/{concertId}", persistedConcert.getId())
                        .accept(MediaType.APPLICATION_JSON))
        // THEN
                .andExpect(status().isNoContent());
        assertThat(concertService.findByConcertId(id)).isNull();
    }
}