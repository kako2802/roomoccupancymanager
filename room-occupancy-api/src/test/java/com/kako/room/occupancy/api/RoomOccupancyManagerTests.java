package com.kako.room.occupancy.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomOccupancyManagerTests {

    private static final BigDecimal ROOM_CATEGORY_DELIMITER = BigDecimal.valueOf(100);

    private static List<BigDecimal> guestsPrices;

    @BeforeAll
    private static void readGuestsPrices() throws URISyntaxException, IOException {
        String guestPricesString = Files.readString(Path.of(RoomOccupancyManagerTests.class.getResource("/guestsPrices.csv").toURI()));

        if (guestPricesString != null && !guestPricesString.isEmpty()) {
            String[] guestPricesArray = guestPricesString.split(",");

            guestsPrices = Arrays.stream(guestPricesArray).map(BigDecimal::new).collect(Collectors.toList());
        } else {
            guestsPrices = Collections.emptyList();
        }
    }

    @Test
    public void shouldReturnZeroRoomsOccupancyAndProfitWhenThereAreNoRoomsAndNoGuestsPrices() {
        // given
        RoomOccupancyManager roomOccupancyManager = new RoomOccupancyManager(new DefaultRoomOccupancyStrategy(ROOM_CATEGORY_DELIMITER));

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
                                                                                       BigDecimal expectedPremiumRoomsProfit, BigDecimal expectedEconomicRoomsProfit) {
        // given
        RoomOccupancyManager roomOccupancyManager = new RoomOccupancyManager(new DefaultRoomOccupancyStrategy(ROOM_CATEGORY_DELIMITER));

        // when
        RoomOccupancyWithProfit roomOccupancyWithProfit = roomOccupancyManager.calculateRoomOccupancyAndProfit(numberOfAvailableEconomicRooms, numberOfAvailablePremiumRooms, guestsPrices);

        // then
        assertEquals(expectedNumberOfOccupiedEconomicRooms, roomOccupancyWithProfit.economicRoomsOccupancy());
        assertEquals(expectedNumberOfOccupiedPremiumRooms, roomOccupancyWithProfit.premiumRoomsOccupancy());
        assertEquals(expectedPremiumRoomsProfit, roomOccupancyWithProfit.premiumRoomProfit());
        assertEquals(expectedEconomicRoomsProfit, roomOccupancyWithProfit.economicRoomProfit());
    }
}
