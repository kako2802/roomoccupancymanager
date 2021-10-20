package com.kako.room.occupancy.api;

import java.math.BigDecimal;

public record RoomOccupancyResult(int numberOfOccupiedRooms, BigDecimal roomsTotalPrice) {
}
