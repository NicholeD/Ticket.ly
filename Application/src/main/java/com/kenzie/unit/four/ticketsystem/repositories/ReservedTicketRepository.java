package com.kenzie.unit.four.ticketsystem.repositories;

import com.kenzie.unit.four.ticketsystem.repositories.model.ReserveTicketRecord;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ReservedTicketRepository extends CrudRepository<ReserveTicketRecord, String> {
    List<ReserveTicketRecord> findByConcertId(String concertId);
}
