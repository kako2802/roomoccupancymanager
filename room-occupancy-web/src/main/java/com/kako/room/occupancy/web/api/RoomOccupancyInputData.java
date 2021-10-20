package com.kako.room.occupancy.web.api;

import java.math.BigDecimal;
import java.util.List;

public record RoomOccupancyInputData (
    int numberOfAvailableEconomicRooms,
    int numberOfAvailablePremiumRooms,
    List<BigDecimal> guestsPrices) {

}
