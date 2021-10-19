package com.kako.room.occupancy.api;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomOccupancyManagerTests {

    @Test
    public void shouldReturnZeroRoomsOccupancyAndProfitWhenThereAreNoRoomsAndNoGuestsPrices() {
        // given
        BigDecimal roomCategoryDelimiter = BigDecimal.valueOf(100);
        RoomOccupancyManager roomOccupancyManager = new RoomOccupancyManager(roomCategoryDelimiter);

        // when
        RoomOccupancyWithProfit roomOccupancyWithProfit = roomOccupancyManager.calculateRoomOccupancyAndProfit(0, 0, emptyList());

        // then
        assertEquals(0, roomOccupancyWithProfit.getEconomicRoomsOccupancy());
        assertEquals(0, roomOccupancyWithProfit.getPremiumRoomsOccupancy());
        assertEquals(BigDecimal.ZERO, roomOccupancyWithProfit.getPremiumRoomProfit());
        assertEquals(BigDecimal.ZERO, roomOccupancyWithProfit.getEconomicRoomProfit());
    }
}
