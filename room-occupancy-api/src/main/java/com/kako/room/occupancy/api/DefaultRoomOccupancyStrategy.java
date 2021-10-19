package com.kako.room.occupancy.api;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultRoomOccupancyStrategy extends AbstractRoomOccupancyStrategy {

    private final BigDecimal roomCategoryMoneyDelimiter;

    public DefaultRoomOccupancyStrategy(BigDecimal roomCategoryMoneyDelimiter) {
        this.roomCategoryMoneyDelimiter = roomCategoryMoneyDelimiter;
    }

    @Override
    protected BigDecimal calculateEconomicRoomsTotalPriceExcludingUpgrades(int numberOfEconomicGuestsToUpgrade, int numberOfOccupiedEconomicRooms,
                                                                           Collection<BigDecimal> economicGuestsPrices) {
        return economicGuestsPrices
                .stream()
                .sorted(Comparator.reverseOrder())
                .skip(numberOfEconomicGuestsToUpgrade)
                .limit(numberOfOccupiedEconomicRooms)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    protected RoomOccupancyResult calculateUpgradedGuestsOccupancyAndProfit(int numberOfAvailablePremiumRooms, int numberOfOccupiedPremiumRooms,
                                                                            Collection<BigDecimal> economicGuestsPrices, int numberOfOccupiedPEconomicRooms) {
        int numberOfEconomicGuestsToUpgrade = Math.min(numberOfAvailablePremiumRooms - numberOfOccupiedPremiumRooms,
                economicGuestsPrices.size() - numberOfOccupiedPEconomicRooms);
        BigDecimal upgradedEconomicGuestsTotalPrice = economicGuestsPrices
                .stream()
                .sorted(Comparator.reverseOrder())
                .limit(numberOfEconomicGuestsToUpgrade)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RoomOccupancyResult(numberOfEconomicGuestsToUpgrade, upgradedEconomicGuestsTotalPrice);
    }

    @Override
    protected RoomOccupancyResult calculateEconomicRoomsOccupancyAndProfit(int numberOfAvailableEconomyRooms, Collection<BigDecimal> economicGuestsPrices) {
        int numberOfOccupiedEconomicRooms = Math.min(numberOfAvailableEconomyRooms, economicGuestsPrices.size());
        BigDecimal economicRoomsTotalPrice = economicGuestsPrices
                .stream()
                .sorted(Comparator.reverseOrder())
                .limit(numberOfOccupiedEconomicRooms)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RoomOccupancyResult(numberOfOccupiedEconomicRooms, economicRoomsTotalPrice);
    }

    @Override
    protected RoomOccupancyResult calculatePremiumRoomsOccupancyAndProfit(int numberOfAvailablePremiumRooms, Collection<BigDecimal> premiumGuestsPrices) {
        int numberOfOccupiedPremiumRooms = Math.min(numberOfAvailablePremiumRooms, premiumGuestsPrices.size());
        BigDecimal premiumRoomsTotalPrice = premiumGuestsPrices
                .stream()
                .sorted(Comparator.reverseOrder())
                .limit(numberOfOccupiedPremiumRooms)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RoomOccupancyResult(numberOfOccupiedPremiumRooms, premiumRoomsTotalPrice);
    }

    @Override
    protected Collection<List<BigDecimal>> partitionGuestsPrices(Collection<BigDecimal> guestsPrices) {
        return guestsPrices
                .stream()
                .collect(Collectors.partitioningBy(price -> price.compareTo(roomCategoryMoneyDelimiter) >= 0))
                .values();
    }
}
