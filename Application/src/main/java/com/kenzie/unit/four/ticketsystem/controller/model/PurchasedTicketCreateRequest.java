package com.kenzie.unit.four.ticketsystem.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class PurchasedTicketCreateRequest {

    @NotEmpty
    @JsonProperty("ticketId")
    private String ticketId;

    @Min(0)
    @JsonProperty("pricePaid")
    private Double pricePaid;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public Double getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(Double pricePaid) {
        this.pricePaid = pricePaid;
    }
}
