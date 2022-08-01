package com.kenzie.unit.four.ticketsystem.service.model;

public class ReservedTicket {

    private final String concertId;
    private final String ticketId;
    private final String dateOfReservation;
    private final Boolean reservationClosed;
    private final String dateReservationClosed;
    private final Boolean ticketPurchased;

    public ReservedTicket(String concertId, String ticketId, String dateOfReservation) {
        this.concertId = concertId;
        this.ticketId = ticketId;
        this.dateOfReservation = dateOfReservation;
        this.reservationClosed = false;
        this.dateReservationClosed = null;
        this.ticketPurchased = false;
    }

    public ReservedTicket(String concertId,
                          String ticketId,
                          String dateOfReservation,
                          Boolean reservationClosed,
                          String dateReservationClosed,
                          Boolean ticketPurchased) {
        this.concertId = concertId;
        this.ticketId = ticketId;
        this.dateOfReservation = dateOfReservation;
        this.reservationClosed = reservationClosed;
        this.dateReservationClosed = dateReservationClosed;
        this.ticketPurchased = ticketPurchased;
    }

    public String getConcertId() {
        return concertId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getDateOfReservation() {
        return dateOfReservation;
    }

    public Boolean getReservationClosed() {
        return reservationClosed;
    }

    public String getDateReservationClosed() {
        return dateReservationClosed;
    }

    public Boolean getTicketPurchased() {
        return ticketPurchased;
    }
}
