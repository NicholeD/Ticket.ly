package com.kenzie.unit.four.ticketsystem.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "PurchasedTicket")
public class PurchasedTicketRecord {
    private  String concertId;
    private  String ticketId;
    private  String dateOfPurchase;
    private  Double pricePaid;

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "ConcertId", attributeName = "ConcertId")
    public String getConcertId() {
        return concertId;
    }

    @DynamoDBHashKey(attributeName = "TicketId")
    public String getTicketId() {
        return ticketId;
    }

    @DynamoDBAttribute(attributeName = "DateOfPurchase")
    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    @DynamoDBAttribute(attributeName = "PricePaid")
    public Double getPricePaid() {
        return pricePaid;
    }

    public void setConcertId(String concertId) {
        this.concertId = concertId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public void setPricePaid(Double pricePaid) {
        this.pricePaid = pricePaid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PurchasedTicketRecord that = (PurchasedTicketRecord) o;
        return Objects.equals(ticketId, that.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId);
    }
}
