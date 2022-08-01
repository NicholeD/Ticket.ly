package com.kenzie.unit.four.ticketsystem.controller.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class ConcertCreateRequest {

    @NotEmpty
    @JsonProperty("name")
    private String name;

    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("date")
    private LocalDate date;

    @Min(0)
    @JsonProperty("ticketBasePrice")
    private Double ticketBasePrice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getTicketBasePrice() {
        return ticketBasePrice;
    }

    public void setTicketBasePrice(Double ticketBasePrice) {
        this.ticketBasePrice = ticketBasePrice;
    }
}
