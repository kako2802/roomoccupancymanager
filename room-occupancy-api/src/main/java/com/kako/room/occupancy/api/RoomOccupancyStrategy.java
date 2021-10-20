package com.kako.room.occupancy.api;

import java.math.BigDecimal;
import java.util.Collection;

public interface RoomOccupancyStrategy {
    RoomOccupancyWithProfit calculateRoomOccupancyAndProfit(int numberOfAvailableEconomyRooms, int numberOfAvailablePremiumRooms, Collection<BigDecimal> guestsPrices);
}
