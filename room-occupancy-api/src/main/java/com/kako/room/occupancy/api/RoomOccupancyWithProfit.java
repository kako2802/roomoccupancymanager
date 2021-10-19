package com.kako.room.occupancy.api;

import java.math.BigDecimal;

public record RoomOccupancyWithProfit(int economicRoomsOccupancy, int premiumRoomsOccupancy,
                                      BigDecimal economicRoomProfit, BigDecimal premiumRoomProfit) {

    public int getPremiumRoomsOccupancy() {
        return premiumRoomsOccupancy;
    }

    public int getEconomicRoomsOccupancy() {
        return economicRoomsOccupancy;
    }

    public BigDecimal getEconomicRoomProfit() {
        return economicRoomProfit;
    }

    public BigDecimal getPremiumRoomProfit() {
        return premiumRoomProfit;
    }
}
