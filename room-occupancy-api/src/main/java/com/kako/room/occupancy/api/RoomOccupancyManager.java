package com.kako.room.occupancy.api;

import java.math.BigDecimal;
import java.util.List;

public class RoomOccupancyManager {

    private RoomOccupancyStrategy roomOccupancyStrategy;

    public RoomOccupancyManager(RoomOccupancyStrategy roomOccupancyStrategy) {
        this.roomOccupancyStrategy = roomOccupancyStrategy;
    }

    public void setRoomOccupancyStrategy(RoomOccupancyStrategy roomOccupancyStrategy) {
        this.roomOccupancyStrategy = roomOccupancyStrategy;
    }

    public RoomOccupancyWithProfit calculateRoomOccupancyAndProfit(int numberOfAvailableEconomyRooms, int numberOfAvailablePremiumRooms, List<BigDecimal> guestsPrices) {
        if(roomOccupancyStrategy != null) {
            return roomOccupancyStrategy.calculateRoomOccupancyAndProfit(numberOfAvailableEconomyRooms, numberOfAvailablePremiumRooms, guestsPrices);
        }

        throw new IllegalArgumentException("Room occupancy strategy is not set");
    }
}
