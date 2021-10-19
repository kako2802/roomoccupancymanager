package com.kako.room.occupancy.api;

import java.math.BigDecimal;
import java.util.List;

public class RoomOccupancyManager {
    private final BigDecimal roomCategoryMoneyDelimiter;

    public RoomOccupancyManager(BigDecimal roomCategoryMoneyDelimiter) {
        this.roomCategoryMoneyDelimiter = roomCategoryMoneyDelimiter;
    }

    public RoomOccupancyWithProfit calculateRoomOccupancyAndProfit(int numberOfAvailableEconomyRooms, int numberOfAvailablePremiumRooms, List<BigDecimal> clientsPrices) {
        if (numberOfAvailableEconomyRooms == 0 && numberOfAvailablePremiumRooms == 0 && clientsPrices.isEmpty()) {
            return new RoomOccupancyWithProfit(0, 0, BigDecimal.ZERO, BigDecimal.ZERO);
        }

        throw new RuntimeException("No implementation");
    }
}
