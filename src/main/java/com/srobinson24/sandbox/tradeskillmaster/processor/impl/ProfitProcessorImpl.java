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
    public int  getCraftingCost(Enchant enchant) {
        logger.trace("Finding Crafting Cost for enchant: {}", enchant.getName());

        int craftingCost = 0;

        for (Map.Entry<TradeSkillMasterItem, Integer> entry : enchant.getCraftingMaterials().entrySet()) {
            final int marketValue = entry.getKey().getRawMarketValue();
            final Integer quantity = entry.getValue();
            craftingCost += marketValue * quantity;
        }

        return craftingCost;
    }

    @Override
    public Integer calculateProfit2(Enchant enchant) {
        final Integer craftingCost = getCraftingCost(enchant);
        final int minimumBuyout = enchant.getRawMinBuyout();
        final int auctionHouseCut = (int)(auctionHousePercent * minimumBuyout);
        final int result = minimumBuyout - auctionHouseCut - craftingCost;
        return result;
    }

    @Override
    public int truncateSilverAndCopper(int value) {
        final String s = Integer.toString(value);
        Preconditions.checkArgument(Integer.toString(value).length() > 4, "Not enough characters in argument: " + value);
        final int endGold = s.length() - 4;
        final String goldOnly = s.substring(0, endGold);
        int intGoldValue = Integer.valueOf(goldOnly);
        final int silverBeginIndex = s.length() - 3;
        final String roundingDigit = s.substring(endGold, silverBeginIndex);
        if (Integer.valueOf(roundingDigit) >= 5) intGoldValue++;
        return intGoldValue;
    }

}
