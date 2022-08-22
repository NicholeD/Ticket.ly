package com.kenzie.unit.four.ticketsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.unit.four.ticketsystem.controller.model.ReservedTicketCreateRequest;
import com.kenzie.unit.four.ticketsystem.controller.model.ReservedTicketResponse;
import com.kenzie.unit.four.ticketsystem.service.ReservedTicketService;
import com.kenzie.unit.four.ticketsystem.service.model.ReservedTicket;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/reservedtickets")
public class ReservedTicketController {

    private ReservedTicketService reservedTicketService;

    ReservedTicketController(ReservedTicketService reservedTicketService) {
        this.reservedTicketService = reservedTicketService;
    }
    
    @PostMapping
    public ResponseEntity<ReservedTicketResponse> reserveTicket(
            @RequestBody ReservedTicketCreateRequest reservedTicketCreateRequest) {

        ReservedTicket reservedTicket = new ReservedTicket(reservedTicketCreateRequest.getConcertId(),
                randomUUID().toString(),
                LocalDateTime.now().toString());
        reservedTicketService.reserveTicket(reservedTicket);
        ReservedTicketResponse response = new ReservedTicketResponse();
        response.setTicketId(reservedTicket.getTicketId());
        response.setDateOfReservation(reservedTicket.getDateOfReservation());
        response.setConcertId(reservedTicket.getConcertId());
        response.setPurchasedTicket(reservedTicket.getTicketPurchased());
        response.setReservationClosed(reservedTicket.getReservationClosed());
        response.setDateReservationClosed(reservedTicket.getDateReservationClosed());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/concerts/{concertId}")
    public ResponseEntity<List<ReservedTicketResponse>> getAllReserveTicketsByConcertId(
            @PathVariable("concertId") String concertId) {

        // Add your code here
        List<ReservedTicket> reservedTickets = reservedTicketService.findByConcertId(concertId);
        List<ReservedTicketResponse> reservedTicketResponses = new ArrayList<>();

        if (reservedTickets.isEmpty() || reservedTickets == null) {
            return ResponseEntity.noContent().build();
        }

        for (ReservedTicket reservedTicket : reservedTickets) {
            ReservedTicketResponse response = new ReservedTicketResponse();
            response.setConcertId(reservedTicket.getConcertId());
            response.setTicketId(reservedTicket.getTicketId());
            response.setDateOfReservation(reservedTicket.getDateOfReservation());
            response.setReservationClosed(reservedTicket.getReservationClosed());
            response.setDateReservationClosed(reservedTicket.getDateReservationClosed());
            response.setPurchasedTicket(reservedTicket.getTicketPurchased());

            reservedTicketResponses.add(response);
        }

        return ResponseEntity.ok(reservedTicketResponses);
    }
}
