package com.srobinson24.sandbox.tradeskillmaster.processor.impl;

import com.google.common.base.Preconditions;
import com.srobinson24.sandbox.tradeskillmaster.domain.Enchant;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.processor.ProfitProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by srobinso on 3/27/2017.
 */
@Service
public class ProfitProcessorImpl implements ProfitProcessor {

    private final Logger logger = LoggerFactory.getLogger(ProfitProcessorImpl.class);

    @Value("${auction.house.percent}")
    private double auctionHousePercent = 0;

    @Override
    public long getCraftingCost(Enchant enchant) {
        logger.trace("Finding Crafting Cost for enchant: {}", enchant.getName());

        long totalCost = 0;

        for (Map.Entry<TradeSkillMasterItem, Double> entry : enchant.getCraftingMaterials().entrySet()) {
            final long marketValue = entry.getKey().getRawMarketValue();
            final Double quantity = entry.getValue();
            final double itemCost = marketValue * quantity;
            totalCost += itemCost;
            logger.trace("item cost of {} is {}, and total cost is {}", entry.getKey().getId(),itemCost,totalCost);
        }

        return totalCost;
    }

    @Override
    public long calculateProfit(Enchant enchant) {
        final long craftingCost = getCraftingCost(enchant);
        final long minimumBuyout = getLowestSalePrice(enchant);
        final long auctionHouseCut = Math.round((auctionHousePercent * minimumBuyout));
        return minimumBuyout - auctionHouseCut - craftingCost;
    }

    @Override
    public long getLowestSalePrice (Enchant enchant) {
        if (enchant.getNumberOfAuctions() > 0) return enchant.getRawMinBuyout();
        else return Math.round(enchant.getRawMarketValue()* 1.10);
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
