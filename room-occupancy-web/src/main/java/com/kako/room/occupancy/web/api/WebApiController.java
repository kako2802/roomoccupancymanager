package com.kako.room.occupancy.web.api;

import com.kako.room.occupancy.api.RoomOccupancyManager;
import com.kako.room.occupancy.api.RoomOccupancyWithProfit;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collection;

@RestController
@RequestMapping("/hotel/rooms")
public class WebApiController {

    private final RoomOccupancyManager roomOccupancyManager;

    public WebApiController(RoomOccupancyManager roomOccupancyManager) {
        this.roomOccupancyManager = roomOccupancyManager;
    }

    @PostMapping("/occupancy")
    public RoomOccupancyWithProfit calculateRoomOccupancyAndProfit(@RequestBody RoomOccupancyInputData roomOccupancyInputData) {
        return roomOccupancyManager.calculateRoomOccupancyAndProfit(roomOccupancyInputData.numberOfAvailableEconomicRooms(), roomOccupancyInputData.numberOfAvailableEconomicRooms(), roomOccupancyInputData.guestsPrices());
    }
}
