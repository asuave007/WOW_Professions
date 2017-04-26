package com.srobinson24.sandbox.tradeskillmaster.processor;

import com.srobinson24.sandbox.tradeskillmaster.domain.Enchant;

/**
 * Created by srobinso on 4/10/2017.
 */
public interface ProfitProcessor {
    int  getCraftingCost(Enchant enchant);

    int calculateProfit(Enchant enchant);

    int truncateSilverAndCopper(int value);
}
