package com.kenzie.unit.four.ticketsystem.repositories;

import com.kenzie.unit.four.ticketsystem.repositories.model.ConcertRecord;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface ConcertRepository extends CrudRepository<ConcertRecord, String> {
}
