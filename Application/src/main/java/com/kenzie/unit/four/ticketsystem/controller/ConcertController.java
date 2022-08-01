package com.kenzie.unit.four.ticketsystem.controller;

import com.kenzie.unit.four.ticketsystem.controller.model.ConcertCreateRequest;
import com.kenzie.unit.four.ticketsystem.controller.model.ConcertResponse;
import com.kenzie.unit.four.ticketsystem.controller.model.ConcertUpdateRequest;
import com.kenzie.unit.four.ticketsystem.service.ConcertService;
import com.kenzie.unit.four.ticketsystem.service.model.Concert;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/concerts")
public class ConcertController {

    private ConcertService concertService;

    ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    @GetMapping("/{concertId}")
    public ResponseEntity<ConcertResponse> searchConcertById(@PathVariable("concertId") String concertId) {
        Concert concert = concertService.findByConcertId(concertId);
        // If there are no concerts, then return a 204
        if (concert == null) {
            return ResponseEntity.notFound().build();
        }
        // Otherwise, convert it into a ConcertResponses and return it
        ConcertResponse concertResponse = createConcertResponse(concert);
        return ResponseEntity.ok(concertResponse);
    }

    @PostMapping
    public ResponseEntity<ConcertResponse> addNewConcert(@RequestBody ConcertCreateRequest concertCreateRequest) {
        Concert concert = new Concert(randomUUID().toString(), 
            concertCreateRequest.getName(), 
            concertCreateRequest.getDate().toString(), 
            concertCreateRequest.getTicketBasePrice(), false);
        concertService.addNewConcert(concert);

        ConcertResponse concertResponse = createConcertResponse(concert);

        return ResponseEntity.created(URI.create("/concerts/" + concertResponse.getId())).body(concertResponse);
    }

    @PutMapping
    public ResponseEntity<ConcertResponse> updateConcert(@RequestBody ConcertUpdateRequest concertUpdateRequest) {
        Concert concert = new Concert(concertUpdateRequest.getId(), 
            concertUpdateRequest.getName(), 
            concertUpdateRequest.getDate().toString(), 
            concertUpdateRequest.getTicketBasePrice(), 
            concertUpdateRequest.getReservationClosed());
        concertService.updateConcert(concert);

        ConcertResponse concertResponse = createConcertResponse(concert);

        return ResponseEntity.ok(concertResponse);
    }

    @GetMapping
    public ResponseEntity<List<ConcertResponse>> getAllConcerts() {
        List<Concert> concerts = concertService.findAllConcerts();
        // If there are no concerts, then return a 204
        if (concerts == null ||  concerts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        // Otherwise, convert the List of Concert objects into a List of ConcertResponses and return it
        List<ConcertResponse> response = new ArrayList<>();
        for (Concert concert : concerts) {
            response.add(this.createConcertResponse(concert));
        }

        return ResponseEntity.ok(response);
    }

    private ConcertResponse createConcertResponse(Concert concert) {
        ConcertResponse concertResponse = new ConcertResponse();
        concertResponse.setId(concert.getId());
        concertResponse.setDate(concert.getDate());
        concertResponse.setTicketBasePrice(concert.getTicketBasePrice());
        concertResponse.setReservationClosed(concert.getReservationClosed());
        concertResponse.setName(concert.getName());
        return concertResponse;
    }

    @DeleteMapping("/{concertId}")
    public ResponseEntity deleteConcertById(@PathVariable("concertId") String concertId) {
        // Your code here
        return null;
    }
}
