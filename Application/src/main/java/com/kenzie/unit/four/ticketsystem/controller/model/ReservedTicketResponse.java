package com.kenzie.unit.four.ticketsystem.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservedTicketResponse {

    @JsonProperty("concertId")
    private String concertId;

    @JsonProperty("ticketId")
    private String ticketId;

    @JsonProperty("dateOfReservation")
    private String dateOfReservation;

    @JsonProperty("reservationClosed")
    private Boolean reservationClosed;

    @JsonProperty("dateReservationClosed")
    private String dateReservationClosed;

    @JsonProperty("purchasedTicket")
    private Boolean purchasedTicket;

    public String getConcertId() {
        return concertId;
    }

    public void setConcertId(String concertId) {
        this.concertId = concertId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getDateOfReservation() {
        return dateOfReservation;
    }

    public void setDateOfReservation(String dateOfReservation) {
        this.dateOfReservation = dateOfReservation;
    }

    public Boolean getReservationClosed() {
        return reservationClosed;
    }

    public void setReservationClosed(Boolean reservationClosed) {
        this.reservationClosed = reservationClosed;
    }

    public String getDateReservationClosed() {
        return dateReservationClosed;
    }

    public void setDateReservationClosed(String dateReservationClosed) {
        this.dateReservationClosed = dateReservationClosed;
    }

    public Boolean getPurchasedTicket() {
        return purchasedTicket;
    }

    public void setPurchasedTicket(Boolean purchasedTicket) {
        this.purchasedTicket = purchasedTicket;
    }
}
