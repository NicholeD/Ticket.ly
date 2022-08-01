package com.kenzie.unit.four.ticketsystem.service.model;

public class Concert {
    private final String id;
    private final String name;
    private final String date;
    private final Double ticketBasePrice;
    private final Boolean reservationClosed;

    public Concert(String id, String name, String date, Double ticketBasePrice, Boolean reservationClosed) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.ticketBasePrice = ticketBasePrice;
        this.reservationClosed = reservationClosed;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Double getTicketBasePrice() {
        return ticketBasePrice;
    }

    public Boolean getReservationClosed() {
        return reservationClosed;
    }
}
