package com.kako.room.occupancy.api;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class RoomOccupancyManager {
    private final BigDecimal roomCategoryMoneyDelimiter;

    public RoomOccupancyManager(BigDecimal roomCategoryMoneyDelimiter) {
        this.roomCategoryMoneyDelimiter = roomCategoryMoneyDelimiter;
    }

    public RoomOccupancyWithProfit calculateRoomOccupancyAndProfit(int numberOfAvailableEconomyRooms, int numberOfAvailablePremiumRooms, List<BigDecimal> clientsPrices) {
        if ((numberOfAvailableEconomyRooms == 0 && numberOfAvailablePremiumRooms == 0) || (clientsPrices == null || clientsPrices.isEmpty())) {
            return new RoomOccupancyWithProfit(0, 0, BigDecimal.ZERO, BigDecimal.ZERO);
        }

        Collection<List<BigDecimal>> economicPremiumPrices = clientsPrices
                .stream()
                .collect(Collectors.partitioningBy(price -> price.compareTo(roomCategoryMoneyDelimiter) >= 0))
                .values();

        Iterator<List<BigDecimal>> economicPremiumPricesIterator = economicPremiumPrices.iterator();
        List<BigDecimal> economicRoomsPrices = economicPremiumPricesIterator.next().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        List<BigDecimal> premiumRoomsPrices = economicPremiumPricesIterator.next().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        int numberOfOccupiedPremiumRooms = Math.min(numberOfAvailablePremiumRooms, premiumRoomsPrices.size());
        BigDecimal premiumRoomsTotalPrice = premiumRoomsPrices
                .stream()
                .limit(numberOfOccupiedPremiumRooms)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int numberOfOccupiedEconomicRooms = Math.min(numberOfAvailableEconomyRooms, economicRoomsPrices.size());

        // upgrade economic customers
        if(numberOfOccupiedPremiumRooms < numberOfAvailablePremiumRooms
                && economicRoomsPrices.size() > numberOfOccupiedEconomicRooms) {
            int numberOfEconomicCustomersToUpgrade = Math.min(numberOfAvailablePremiumRooms - numberOfOccupiedPremiumRooms, economicRoomsPrices.size() - numberOfOccupiedEconomicRooms);
            BigDecimal upgradedEconomicCustomersTotalPrice = economicRoomsPrices
                    .stream()
                    .limit(numberOfEconomicCustomersToUpgrade)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal economicRoomsTotalPriceExcludingUpgrades = economicRoomsPrices
                    .stream()
                    .skip(numberOfEconomicCustomersToUpgrade)
                    .limit(numberOfOccupiedEconomicRooms)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return new RoomOccupancyWithProfit(numberOfOccupiedEconomicRooms, numberOfOccupiedPremiumRooms + numberOfEconomicCustomersToUpgrade,
                    economicRoomsTotalPriceExcludingUpgrades, premiumRoomsTotalPrice.add(upgradedEconomicCustomersTotalPrice));
        }

        BigDecimal economicRoomsTotalPrice = economicRoomsPrices
                .stream()
                .limit(numberOfOccupiedEconomicRooms)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RoomOccupancyWithProfit(numberOfOccupiedEconomicRooms, numberOfOccupiedPremiumRooms,
                economicRoomsTotalPrice, premiumRoomsTotalPrice);
    }
}
