package com.srobinson24.sandbox.tradeskillmaster.processor.impl;

import com.google.common.base.Preconditions;
import com.srobinson24.sandbox.tradeskillmaster.domain.*;
import com.srobinson24.sandbox.tradeskillmaster.processor.ProfitProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by srobinso on 3/27/2017.
 */
@Service
public class ProfitProcessorImpl implements ProfitProcessor {

    private final Logger logger = LoggerFactory.getLogger(ProfitProcessorImpl.class);

    @Value("${auction.house.percent}")
    private double auctionHousePercent = 0;

    @Override
    public double getCraftingCost(CraftableItem craftableItem) {
        logger.trace("Finding Crafting Cost for craftableItem: {}", craftableItem);

        if (CraftingType.INSCRIPTION_PIGMENT.equals(craftableItem.getCraftingType())) return pricePigmentCraftCost((InscriptionPigment) craftableItem);
        if (CraftingType.INSCRIPTION_INK.equals(craftableItem.getCraftingType())) return priceInkCraftCost((InscriptionInk) craftableItem);

        long totalCost = 0;

        for (Map.Entry<TradeSkillMasterItem, Double> entry : craftableItem.getCraftingMaterials().entrySet()) {
            final long marketValue = entry.getKey().getRawMarketValue();
            final Double quantity = entry.getValue();
            final double itemCost = marketValue * quantity;
            totalCost += itemCost;
            logger.trace("item cost of {} is {}, and total cost is {}", entry.getKey().getId(),itemCost,totalCost);
        }

        return totalCost;
    }

    private double lowestPigmentCost (InscriptionPigment pigment) {
        if (pigment.isCheaperToCraft()) return pricePigmentCraftCost(pigment);
        else return pigment.getRawMinBuyout();
    }

    private double priceInkCraftCost(InscriptionInk ink) {
        logger.trace("Item is an ink: {}", ink);
//        final InscriptionPigment pigment = ink.getCraftingMaterials().entrySet().stream().findFirst().orElseThrow(NoSuchElementException::new);
        final TradeSkillMasterItem pigment =
                ink.getCraftingMaterials().entrySet().stream().findFirst().orElseThrow(NoSuchElementException::new).getKey();
        final double lowestPigmentCost = lowestPigmentCost((InscriptionPigment) pigment);
        if (ink.getRawMinBuyout() < lowestPigmentCost) ink.setCheaperToCraft(true);
        else ink.setCheaperToCraft(false);
        return lowestPigmentCost < ink.getRawMinBuyout() ? lowestPigmentCost : ink.getRawMinBuyout();
    }

    private double pricePigmentCraftCost(InscriptionPigment pigment) {
        logger.trace("Item is a pigment: {}", pigment);

        Map.Entry<TradeSkillMasterItem, Double> cheapestHerb = findCheapestHerb(pigment);

        pigment.setCheapestHerb(cheapestHerb.getKey());
        final double craftingCost = cheapestHerb.getKey().getRawMinBuyout() * cheapestHerb.getValue();
        if (craftingCost < pigment.getRawMinBuyout()) pigment.setCheaperToCraft(true);
        return craftingCost;
    }

    private Map.Entry<TradeSkillMasterItem, Double> findCheapestHerb(InscriptionPigment inscriptionPigment) {

        Preconditions.checkArgument(
                inscriptionPigment.getHerbMillingRateMap() != null || !inscriptionPigment.getHerbMillingRateMap().isEmpty(),
                "Inscription Pigment had an empty Herb Milling Rate Map: " + inscriptionPigment);

        final Map.Entry<TradeSkillMasterItem, Double> cheapestHerbByRate = inscriptionPigment
                .getHerbMillingRateMap()
                .entrySet()
                .stream()
                .min(Comparator.comparing(entry -> (entry.getValue() * entry.getKey().getRawMinBuyout())))
                .orElseThrow(NoSuchElementException::new);
        return cheapestHerbByRate;

    }

    @Override
    public double calculateProfit(CraftableItem craftableItem) {
        final double craftingCost = getCraftingCost(craftableItem);
        final long minimumBuyout = getLowestSalePrice(craftableItem);
        final long auctionHouseCut = Math.round((auctionHousePercent * minimumBuyout));
        logger.trace("Auction House cut is: {}", auctionHouseCut);
        final double calculatedProfit = minimumBuyout - auctionHouseCut - craftingCost;
        logger.trace("Calculated Profit is: {}", calculatedProfit);
        return calculatedProfit;
    }

    @Override
    public long getLowestSalePrice (CraftableItem craftableItem) {
        long lowestSaleablePrice;
        if (craftableItem.getNumberOfAuctions() > 0) {
            lowestSaleablePrice = craftableItem.getRawMinBuyout();
        }
        else {
            lowestSaleablePrice = Math.round(craftableItem.getRawMarketValue() * 1.10);
        }
        logger.trace("Lowest Saleable Price determined to be: {}", lowestSaleablePrice);
        return lowestSaleablePrice;
    }

    @Override
    public long truncateSilverAndCopper(long value) {
        final String s = Long.toString(value);
        Preconditions.checkArgument(Long.toString(value).length() > 4, "Not enough characters in argument: " + value);
        final int endGold = s.length() - 4;
        final String goldOnly = s.substring(0, endGold);
        long longGoldValue = Long.valueOf(goldOnly);
        final int silverBeginIndex = s.length() - 3;
        final String roundingDigit = s.substring(endGold, silverBeginIndex);
        if (Integer.valueOf(roundingDigit) >= 5) longGoldValue++;
        return longGoldValue;
    }

}
