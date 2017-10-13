package com.srobinson24.sandbox.tradeskillmaster.service;

import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

/**
 * Created by srobinso on 4/18/2017.
 */
public interface ItemUpdateService {

    TradeSkillMasterItem fetchUpdateItemInformation(int id);

    void updateItemsFromRemoteService(Set<TradeSkillMasterItem> itemsToUpdate) throws URISyntaxException;

    Map<Integer, TradeSkillMasterItem> update();
}
