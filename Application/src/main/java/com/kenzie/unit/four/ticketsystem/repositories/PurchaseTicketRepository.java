package com.kenzie.unit.four.ticketsystem.repositories;

import com.kenzie.unit.four.ticketsystem.repositories.model.PurchasedTicketRecord;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PurchaseTicketRepository extends CrudRepository<PurchasedTicketRecord, String> {
    List<PurchasedTicketRecord> findByConcertId(String concertId);
}
