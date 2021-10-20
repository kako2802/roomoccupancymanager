package com.kako.room.occupancy.api;

import java.math.BigDecimal;

public record RoomOccupancyWithProfit(int economicRoomsOccupancy, int premiumRoomsOccupancy,
                                      BigDecimal economicRoomProfit, BigDecimal premiumRoomProfit) {
}
