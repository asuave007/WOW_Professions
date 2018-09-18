package com.srobinson24.sandbox.tradeskillmaster.service;

import com.srobinson24.sandbox.tradeskillmaster.domain.CraftableItem;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * Created by srobinso on 3/26/2017.
 */
public interface PricingService {

    void getPricedEnchants();

    //fixme: this should be placed elsewhere
    Map<TradeSkillMasterItem, Double> calculateMats(Set<CraftableItem> profitableToCraftCraftableItems);

    SortedSet<CraftableItem> sortByProfit(Set<CraftableItem> craftableItems);
}
