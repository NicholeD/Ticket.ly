package com.kenzie.unit.four.ticketsystem.service.model;

public class PurchasedTicket {

    private final String concertId;
    private final String ticketId;
    private final String dateOfPurchase;
    private final Double pricePaid;

    public PurchasedTicket(String concertId, String ticketId, String dateOfPurchase, Double pricePaid) {
        this.concertId = concertId;
        this.ticketId = ticketId;
        this.dateOfPurchase = dateOfPurchase;
        this.pricePaid = pricePaid;
    }

    public String getConcertId() {
        return concertId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public Double getPricePaid() {
        return pricePaid;
    }
}
