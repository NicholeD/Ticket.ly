package com.kenzie.unit.four.ticketsystem.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "ReservedTicket")
public class ReserveTicketRecord {
    private String concertId;
    private String ticketId;
    private String dateOfReservation;
    private Boolean reservationClosed;
    private String dateReservationClosed;
    private Boolean purchasedTicket;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "ConcertId", attributeName = "ConcertId")
    public String getConcertId() {
        return concertId;
    }

    @DynamoDBHashKey(attributeName = "TicketId")
    public String getTicketId() {
        return ticketId;
    }

    @DynamoDBAttribute(attributeName = "DateOfReservation")
    public String getDateOfReservation() {
        return dateOfReservation;
    }

    @DynamoDBAttribute(attributeName = "ReservationClosed")
    public Boolean getReservationClosed() {
        return reservationClosed;
    }

    @DynamoDBAttribute(attributeName = "DateReservationClosed")
    public String getDateReservationClosed() {
        return dateReservationClosed;
    }

    @DynamoDBAttribute(attributeName = "PurchasedTicket")
    public Boolean getPurchasedTicket() {
        return purchasedTicket;
    }

    public void setConcertId(String concertId) {
        this.concertId = concertId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setDateOfReservation(String dateOfReservation) {
        this.dateOfReservation = dateOfReservation;
    }

    public void setReservationClosed(Boolean reservationClosed) {
        this.reservationClosed = reservationClosed;
    }

    public void setDateReservationClosed(String dateReservationClosed) {
        this.dateReservationClosed = dateReservationClosed;
    }

    public void setPurchasedTicket(Boolean purchasedTicket) {
        this.purchasedTicket = purchasedTicket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReserveTicketRecord that = (ReserveTicketRecord) o;
        return Objects.equals(ticketId, that.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId);
    }
}
