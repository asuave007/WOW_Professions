package com.srobinson24.sandbox.tradeskillmaster.service;

import com.srobinson24.sandbox.tradeskillmaster.domain.Enchant;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;

import java.util.Map;
import java.util.Set;

/**
 * Created by srobinso on 3/24/2017.
 */
public interface ItemService {

    Map<Integer, TradeSkillMasterItem> readCraftingItems();

    Set<Enchant> readEnchants(Map <Integer, TradeSkillMasterItem> craftingMaterialsKeyedOnId);

    Map<Integer, TradeSkillMasterItem> updateItemInformation(Set <TradeSkillMasterItem> enchants);

    Set<Integer> findItemsToUpdate(Set<TradeSkillMasterItem> enchantsFromFile);

    void callUpdateService(Set<TradeSkillMasterItem> itemsToUpdate, Set<Integer> itemIdsToUpdate);

}
