package com.srobinson24.sandbox.tradeskillmaster.processor;

import com.srobinson24.sandbox.tradeskillmaster.domain.CraftableItem;

/**
 * Created by srobinso on 4/10/2017.
 */
public interface ProfitProcessor {
    double getCraftingCost(CraftableItem craftableItem);

    double calculateProfit(CraftableItem craftableItem);

    long getLowestSalePrice(CraftableItem craftableItem);

    long truncateSilverAndCopper(long value);
}
