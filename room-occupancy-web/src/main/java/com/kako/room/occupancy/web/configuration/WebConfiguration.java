package com.kako.room.occupancy.web.configuration;

import com.kako.room.occupancy.api.DefaultRoomOccupancyStrategy;
import com.kako.room.occupancy.api.RoomOccupancyManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class WebConfiguration {

    @Value("${room.occupancy.delimiter}")
    private BigDecimal roomOccupancyDelimiter;

    @Bean
    public RoomOccupancyManager roomOccupancyManager() {
        return new RoomOccupancyManager(new DefaultRoomOccupancyStrategy(roomOccupancyDelimiter));
    }
}
