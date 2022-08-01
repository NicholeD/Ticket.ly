package com.kenzie.unit.four.ticketsystem.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class ReservedTicketCreateRequest {

    @NotEmpty
    @JsonProperty("concertId")
    private String concertId;

    public String getConcertId() {
        return concertId;
    }

    public void setConcertId(String concertId) {
        this.concertId = concertId;
    }
}
