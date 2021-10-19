package com.kako.room.occupancy.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomOccupancyManagerTests {

    private static final List<BigDecimal> GUESTS_PRICES =
            Arrays.asList(
                    BigDecimal.valueOf(23),
                    BigDecimal.valueOf(45),
                    BigDecimal.valueOf(155),
                    BigDecimal.valueOf(374),
                    BigDecimal.valueOf(22),
                    BigDecimal.valueOf(99.99),
                    BigDecimal.valueOf(100),
                    BigDecimal.valueOf(101),
                    BigDecimal.valueOf(115),
                    BigDecimal.valueOf(209));

    @Test
    public void shouldReturnZeroRoomsOccupancyAndProfitWhenThereAreNoRoomsAndNoGuestsPrices() {
        // given
        BigDecimal roomCategoryDelimiter = BigDecimal.valueOf(100);
        RoomOccupancyManager roomOccupancyManager = new RoomOccupancyManager(new DefaultRoomOccupancyStrategy(roomCategoryDelimiter));

        // when
        RoomOccupancyWithProfit roomOccupancyWithProfit = roomOccupancyManager.calculateRoomOccupancyAndProfit(0, 0, emptyList());

        // then
        assertEquals(0, roomOccupancyWithProfit.economicRoomsOccupancy());
        assertEquals(0, roomOccupancyWithProfit.premiumRoomsOccupancy());
        assertEquals(BigDecimal.ZERO, roomOccupancyWithProfit.premiumRoomProfit());
        assertEquals(BigDecimal.ZERO, roomOccupancyWithProfit.economicRoomProfit());
    }

    @ParameterizedTest
    @CsvSource({
            "3, 3, 3, 3, 738, 167.99",
            "5, 7, 4, 6, 1054, 189.99",
            "7, 2, 4, 2, 583, 189.99",
            "1, 7, 1, 7, 1153.99, 45",
            "3, 8, 3, 7, 1153.99, 90"})
    public void shouldReturnAllRoomsOccupancyAndProfitWhenThereAreRoomsAndGuestsPrices(int numberOfAvailableEconomicRooms, int numberOfAvailablePremiumRooms,
                                                                                       int expectedNumberOfOccupiedEconomicRooms, int expectedNumberOfOccupiedPremiumRooms,
                                                                                       BigDecimal expectedPremiumRoomsProfit, BigDecimal expectedEconomicRoomsProfit){
        // given
        BigDecimal roomCategoryDelimiter = BigDecimal.valueOf(100);
        RoomOccupancyManager roomOccupancyManager = new RoomOccupancyManager(new DefaultRoomOccupancyStrategy(roomCategoryDelimiter));

        // when
        RoomOccupancyWithProfit roomOccupancyWithProfit = roomOccupancyManager.calculateRoomOccupancyAndProfit(numberOfAvailableEconomicRooms, numberOfAvailablePremiumRooms, GUESTS_PRICES);

        // then
        assertEquals(expectedNumberOfOccupiedEconomicRooms, roomOccupancyWithProfit.economicRoomsOccupancy());
        assertEquals(expectedNumberOfOccupiedPremiumRooms, roomOccupancyWithProfit.premiumRoomsOccupancy());
        assertEquals(expectedPremiumRoomsProfit, roomOccupancyWithProfit.premiumRoomProfit());
        assertEquals(expectedEconomicRoomsProfit, roomOccupancyWithProfit.economicRoomProfit());
    }
}
