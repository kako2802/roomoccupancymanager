package com.kako.room.occupancy.api;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractRoomOccupancyStrategy implements RoomOccupancyStrategy {

    @Override
    public RoomOccupancyWithProfit calculateRoomOccupancyAndProfit(int numberOfAvailableEconomyRooms, int numberOfAvailablePremiumRooms, Collection<BigDecimal> guestsPrices) {
        if (isTheNumberOfRoomsIncorrect(numberOfAvailableEconomyRooms, numberOfAvailablePremiumRooms) ||
                isGuestsPricesIncorrect(guestsPrices)) {
            return new RoomOccupancyWithProfit(0, 0, BigDecimal.ZERO, BigDecimal.ZERO);
        }

        Collection<List<BigDecimal>> partitionedGuestsPrices = partitionGuestsPrices(guestsPrices);

        Iterator<List<BigDecimal>> partitionedGuestsPricesIterator = partitionedGuestsPrices.iterator();
        Collection<BigDecimal> economicGuestsPrices = partitionedGuestsPricesIterator.next();
        Collection<BigDecimal> premiumGuestsPrices = partitionedGuestsPricesIterator.next();

        RoomOccupancyResult premiumRoomsOccupancyResult = calculatePremiumRoomsOccupancyAndProfit(numberOfAvailablePremiumRooms, premiumGuestsPrices);
        RoomOccupancyResult economicRoomsOccupancyResult = calculateEconomicRoomsOccupancyAndProfit(numberOfAvailableEconomyRooms, economicGuestsPrices);

        if(isUpgradeAvailableForEconomicGuests(numberOfAvailablePremiumRooms, economicGuestsPrices,
                premiumRoomsOccupancyResult, economicRoomsOccupancyResult)) {

            RoomOccupancyResult upgradedEconomicGuestsOccupancyResult = calculateUpgradedGuestsOccupancyAndProfit(numberOfAvailablePremiumRooms, premiumRoomsOccupancyResult.numberOfOccupiedRooms(),
                    economicGuestsPrices, economicRoomsOccupancyResult.numberOfOccupiedRooms());

            BigDecimal economicRoomsTotalPriceExcludingUpgrades = calculateEconomicRoomsTotalPriceExcludingUpgrades(
                    upgradedEconomicGuestsOccupancyResult.numberOfOccupiedRooms(), economicRoomsOccupancyResult.numberOfOccupiedRooms(),
                    economicGuestsPrices);

            return new RoomOccupancyWithProfit(economicRoomsOccupancyResult.numberOfOccupiedRooms(),
                    premiumRoomsOccupancyResult.numberOfOccupiedRooms() + upgradedEconomicGuestsOccupancyResult.numberOfOccupiedRooms(),
                    economicRoomsTotalPriceExcludingUpgrades,
                    premiumRoomsOccupancyResult.roomsTotalPrice().add(upgradedEconomicGuestsOccupancyResult.roomsTotalPrice()));
        } else {
            return new RoomOccupancyWithProfit(economicRoomsOccupancyResult.numberOfOccupiedRooms(), premiumRoomsOccupancyResult.numberOfOccupiedRooms(),
                    economicRoomsOccupancyResult.roomsTotalPrice(), premiumRoomsOccupancyResult.roomsTotalPrice());
        }
    }

    private boolean isGuestsPricesIncorrect(Collection<BigDecimal> guestsPrices) {
        return guestsPrices == null || guestsPrices.isEmpty();
    }

    private boolean isTheNumberOfRoomsIncorrect(int numberOfAvailableEconomyRooms, int numberOfAvailablePremiumRooms) {
        return numberOfAvailableEconomyRooms <= 0 && numberOfAvailablePremiumRooms <= 0;
    }

    private boolean isUpgradeAvailableForEconomicGuests(int numberOfAvailablePremiumRooms, Collection<BigDecimal> economicGuestsPrices, RoomOccupancyResult premiumRoomsOccupancyResult, RoomOccupancyResult economicRoomsOccupancyResult) {
        return premiumRoomsOccupancyResult.numberOfOccupiedRooms() < numberOfAvailablePremiumRooms
                && economicGuestsPrices.size() > economicRoomsOccupancyResult.numberOfOccupiedRooms();
    }

    protected abstract BigDecimal calculateEconomicRoomsTotalPriceExcludingUpgrades(int numberOfEconomicGuestsToUpgrade, int numberOfOccupiedEconomicRooms,
                                                                                    Collection<BigDecimal> economicGuestsPrices);

    protected abstract RoomOccupancyResult calculateUpgradedGuestsOccupancyAndProfit(int numberOfAvailablePremiumRooms, int numberOfOccupiedPremiumRooms, Collection<BigDecimal> economicGuestsPrices, int numberOfOccupiedPremiumRooms1);

    protected abstract RoomOccupancyResult calculateEconomicRoomsOccupancyAndProfit(int numberOfAvailableEconomyRooms, Collection<BigDecimal> economicGuestsPrices);

    protected abstract RoomOccupancyResult calculatePremiumRoomsOccupancyAndProfit(int numberOfAvailablePremiumRooms, Collection<BigDecimal> premiumGuestsPrices);

    protected abstract Collection<List<BigDecimal>> partitionGuestsPrices(Collection<BigDecimal> guestsPrices);
}
