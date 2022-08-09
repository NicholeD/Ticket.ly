package com.kenzie.unit.four.ticketsystem.controller;

import com.kenzie.unit.four.ticketsystem.controller.model.ReservedTicketCreateRequest;
import com.kenzie.unit.four.ticketsystem.controller.model.ReservedTicketResponse;
import com.kenzie.unit.four.ticketsystem.service.ReservedTicketService;
import com.kenzie.unit.four.ticketsystem.service.model.ReservedTicket;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/reservedtickets")
public class ReservedTicketController {

    private ReservedTicketService reservedTicketService;

    ReservedTicketController(ReservedTicketService reservedTicketService) {
        this.reservedTicketService = reservedTicketService;
    }

    // TODO - Task 2: reserveTicket() - POST
    @PostMapping
    public ResponseEntity<ReservedTicketResponse> reserveTicket(
            @RequestBody ReservedTicketCreateRequest reservedTicketCreateRequest) {
        String concertId = reservedTicketCreateRequest.getConcertId();
        String ticketId = randomUUID().toString();
        String dateOfReservation = LocalDateTime.now().toString();

        ReservedTicket reservedTicket = new ReservedTicket(concertId, ticketId, dateOfReservation);
        reservedTicketService.reserveTicket(reservedTicket);
        ReservedTicketResponse response = new ReservedTicketResponse();
        response.setTicketId(reservedTicket.getTicketId());
        response.setDateOfReservation(reservedTicket.getDateOfReservation());
        response.setConcertId(reservedTicket.getConcertId());

        // Return your ReservedTicketResponse instead of null
        return ResponseEntity.ok(response);
    }

    // TODO - Task 3: getAllReserveTicketsByConcertId() - GET `/concerts/{concertId}`
    // Add the correct annotation
    public ResponseEntity<List<ReservedTicketResponse>> getAllReserveTicketsByConcertId(
            @PathVariable("concertId") String concertId) {

        // Add your code here

        // Return your List<ReservedTicketResponse> instead of null
        return ResponseEntity.ok(null);
    }

}
