package com.srobinson24.sandbox.tradeskillmaster.service;

import com.srobinson24.sandbox.tradeskillmaster.domain.Enchant;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;

import java.util.Map;
import java.util.Set;

/**
 * Created by srobinso on 3/24/2017.
 */
public interface ItemService {

    Map<Integer, TradeSkillMasterItem> readCraftingItemsFromFile();

    Set<Enchant> readEnchantsFromFile(Map<Integer, TradeSkillMasterItem> craftingMaterialsKeyedOnId);

    void updateItemInformation(Set<TradeSkillMasterItem> enchants);

    void updateItemsToPrice(Set<TradeSkillMasterItem> enchantsFromFile);

}
