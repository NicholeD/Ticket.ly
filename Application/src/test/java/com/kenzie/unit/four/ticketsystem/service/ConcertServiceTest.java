package com.kenzie.unit.four.ticketsystem.service;

import com.kenzie.unit.four.ticketsystem.config.CacheStore;
import com.kenzie.unit.four.ticketsystem.repositories.ConcertRepository;
import com.kenzie.unit.four.ticketsystem.repositories.model.ConcertRecord;
import com.kenzie.unit.four.ticketsystem.service.model.Concert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConcertServiceTest {
    private ConcertRepository concertRepository;
    private CacheStore cacheStore;
    private ConcertService concertService;

    @BeforeEach
    void setup() {
        concertRepository = mock(ConcertRepository.class);
        cacheStore = mock(CacheStore.class);
        concertService = new ConcertService(concertRepository, cacheStore);
    }

    /** ------------------------------------------------------------------------
     *  concertService.findAllConcerts
     *  ------------------------------------------------------------------------ **/

    @Test
    void findAllConcerts_two_concerts() {
        // GIVEN
        ConcertRecord record1 = new ConcertRecord();
        record1.setId(randomUUID().toString());
        record1.setName("concertname1");
        record1.setDate("recorddate2");
        record1.setTicketBasePrice(10.0);
        record1.setReservationClosed(true);

        ConcertRecord record2 = new ConcertRecord();
        record2.setId(randomUUID().toString());
        record2.setName("concertname2");
        record2.setDate("recorddate2");
        record2.setTicketBasePrice(15.0);
        record2.setReservationClosed(false);

        List<ConcertRecord> recordList = new ArrayList<>();
        recordList.add(record1);
        recordList.add(record2);
        when(concertRepository.findAll()).thenReturn(recordList);

        // WHEN
        List<Concert> concerts = concertService.findAllConcerts();

        // THEN
        Assertions.assertNotNull(concerts, "The concert list is returned");
        Assertions.assertEquals(2, concerts.size(), "There are two concerts");

        for (Concert concert : concerts) {
            if (concert.getId() == record1.getId()) {
                Assertions.assertEquals(record1.getId(), concert.getId(), "The concert id matches");
                Assertions.assertEquals(record1.getName(), concert.getName(), "The concert name matches");
                Assertions.assertEquals(record1.getDate(), concert.getDate(), "The concert date matches");
                Assertions.assertEquals(record1.getTicketBasePrice(), concert.getTicketBasePrice(), "The concert ticket price matches");
                Assertions.assertEquals(record1.getReservationClosed(), concert.getReservationClosed(), "The concert reservation closed flag matches");
            } else if (concert.getId() == record2.getId()) {
                Assertions.assertEquals(record2.getId(), concert.getId(), "The concert id matches");
                Assertions.assertEquals(record2.getName(), concert.getName(), "The concert name matches");
                Assertions.assertEquals(record2.getDate(), concert.getDate(), "The concert date matches");
                Assertions.assertEquals(record2.getTicketBasePrice(), concert.getTicketBasePrice(), "The concert ticket price matches");
                Assertions.assertEquals(record2.getReservationClosed(), concert.getReservationClosed(), "The concert reservation closed flag matches");
            } else {
                Assertions.assertTrue(false, "Concert returned that was not in the records!");
            }
        }
    }

    /** ------------------------------------------------------------------------
     *  concertService.findByConcertId
     *  ------------------------------------------------------------------------ **/

    @Test
    void findByConcertId() {
        // GIVEN
        String concertId = randomUUID().toString();

        ConcertRecord record = new ConcertRecord();
        record.setId(concertId);
        record.setName("concertname");
        record.setDate("recorddate");
        record.setTicketBasePrice(10.0);
        record.setReservationClosed(true);
        when(concertRepository.findById(concertId)).thenReturn(Optional.of(record));
        // WHEN
        Concert concert = concertService.findByConcertId(concertId);

        // THEN
        Assertions.assertNotNull(concert, "The concert is returned");
        Assertions.assertEquals(record.getId(), concert.getId(), "The concert id matches");
        Assertions.assertEquals(record.getName(), concert.getName(), "The concert name matches");
        Assertions.assertEquals(record.getDate(), concert.getDate(), "The concert date matches");
        Assertions.assertEquals(record.getTicketBasePrice(), concert.getTicketBasePrice(), "The concert ticket price matches");
        Assertions.assertEquals(record.getReservationClosed(), concert.getReservationClosed(), "The concert reservation closed flag matches");
    }

    /** ------------------------------------------------------------------------
     *  concertService.addNewConcert
     *  ------------------------------------------------------------------------ **/

    @Test
    void addNewConcert() {
        // GIVEN
        String concertId = randomUUID().toString();

        Concert concert = new Concert(concertId, "concertname", "recorddate", 10.0, false);

        ArgumentCaptor<ConcertRecord> concertRecordCaptor = ArgumentCaptor.forClass(ConcertRecord.class);

        // WHEN
        Concert returnedConcert = concertService.addNewConcert(concert);

        // THEN
        Assertions.assertNotNull(returnedConcert);

        verify(concertRepository).save(concertRecordCaptor.capture());

        ConcertRecord record = concertRecordCaptor.getValue();

        Assertions.assertNotNull(record, "The concert record is returned");
        Assertions.assertEquals(record.getId(), concert.getId(), "The concert id matches");
        Assertions.assertEquals(record.getName(), concert.getName(), "The concert name matches");
        Assertions.assertEquals(record.getDate(), concert.getDate(), "The concert date matches");
        Assertions.assertEquals(record.getTicketBasePrice(), concert.getTicketBasePrice(), "The concert ticket price matches");
        Assertions.assertEquals(record.getReservationClosed(), concert.getReservationClosed(), "The concert reservation closed flag matches");
    }

    /** ------------------------------------------------------------------------
     *  concertService.updateConcert
     *  ------------------------------------------------------------------------ **/

    // Write additional tests here

    /** ------------------------------------------------------------------------
     *  concertService.deleteConcert
     *  ------------------------------------------------------------------------ **/

    // Write additional tests here

}
