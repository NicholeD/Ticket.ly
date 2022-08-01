package com.kenzie.unit.four.ticketsystem.service;

import com.kenzie.unit.four.ticketsystem.IntegrationTest;
import com.kenzie.unit.four.ticketsystem.service.model.Concert;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import com.kenzie.unit.four.ticketsystem.config.CacheStore;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class CacheManagerTest {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    ConcertService concertService;

    @Autowired
    private CacheStore Cache;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    @Test
    public void concertCache_InsertIntoCache() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String date = LocalDate.now().toString();
        Double ticketBasePrice = 80.0;

        Concert concert = new Concert(id, name, date, ticketBasePrice, false);
        concertService.addNewConcert(concert);
        concertService.findByConcertId(id);

        Concert concertFromCache = Cache.get(concert.getId());

        assertThat(concertFromCache).isNotNull();
        assertThat(concertFromCache.getId()).isEqualTo(id);
    }

    @Test
    public void concertCacheUpdate_EvictFromCache() throws Exception {
        String id = UUID.randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String date = LocalDate.now().toString();
        Double ticketBasePrice = 80.0;


        Concert concert = new Concert(id, name, date, ticketBasePrice, false);
        concertService.addNewConcert(concert);
        concertService.findByConcertId(id);

        Concert concertFromCache = Cache.get(concert.getId());

        concertService.updateConcert(concert);

        Concert concertFromCacheAfterUpdate = Cache.get(concert.getId());

        assertThat(concertFromCache).isNotNull();
        assertThat(concertFromCache.getId()).isEqualTo(id);
        assertThat(concertFromCacheAfterUpdate).isNull();
    }
}
