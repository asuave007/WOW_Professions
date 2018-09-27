package com.srobinson24.sandbox.tradeskillmaster.service;

import com.srobinson24.sandbox.tradeskillmaster.domain.CraftableItem;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by srobinso on 3/26/2017.
 */
public interface PricingService {

    void getPricedCrafts();

    //fixme: this should be placed elsewhere
    Map<TradeSkillMasterItem, Double> calculateMats(Set<CraftableItem> profitableToCraftCraftableItems);

    List<CraftableItem> sortByProfit(Set<CraftableItem> craftableItems);
}
