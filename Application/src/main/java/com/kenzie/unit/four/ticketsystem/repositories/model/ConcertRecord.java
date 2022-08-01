package com.kenzie.unit.four.ticketsystem.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "Concert")
public class ConcertRecord {

    private String id;
    private String name;
    private String date;
    private Double ticketBasePrice;
    private Boolean reservationClosed;

    @DynamoDBHashKey(attributeName = "Id")
    public String getId() {
        return id;
    }

    @DynamoDBAttribute(attributeName = "Name")
    public String getName() {
        return name;
    }

    @DynamoDBAttribute(attributeName = "Date")
    public String getDate() {
        return date;
    }

    @DynamoDBAttribute(attributeName = "TicketBasePrice")
    public Double getTicketBasePrice() {
        return ticketBasePrice;
    }

    @DynamoDBAttribute(attributeName = "ReservationClosed")
    public Boolean getReservationClosed() {
        return reservationClosed;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTicketBasePrice(Double ticketBasePrice) {
        this.ticketBasePrice = ticketBasePrice;
    }

    public void setReservationClosed(Boolean reservationClosed) {
        this.reservationClosed = reservationClosed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConcertRecord concertRecord = (ConcertRecord) o;
        return Objects.equals(id, concertRecord.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
