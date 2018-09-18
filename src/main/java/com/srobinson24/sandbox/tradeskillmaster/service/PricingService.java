package com.srobinson24.sandbox.tradeskillmaster.service;

import com.srobinson24.sandbox.tradeskillmaster.domain.CraftableItem;

import java.util.Set;
import java.util.SortedSet;

/**
 * Created by srobinso on 3/26/2017.
 */
public interface PricingService {

    void getPricedEnchants();

    SortedSet<CraftableItem> sortByProfit(Set<CraftableItem> craftableItems);
}
