package com.srobinson24.sandbox.tradeskillmaster.processor;

import com.srobinson24.sandbox.tradeskillmaster.domain.Enchant;

/**
 * Created by srobinso on 4/10/2017.
 */
public interface ProfitProcessor {
    long getCraftingCost(Enchant enchant);

    long calculateProfit(Enchant enchant);

    long getLowestSalePrice(Enchant enchant);

    long truncateSilverAndCopper(long value);
}
