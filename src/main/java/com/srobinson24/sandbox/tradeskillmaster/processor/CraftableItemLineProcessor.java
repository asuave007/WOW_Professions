package com.srobinson24.sandbox.tradeskillmaster.processor;

import com.google.common.io.LineProcessor;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;

import java.util.Map;

/**
 * Created by srobinso on 4/12/2017.
 */
public interface CraftableItemLineProcessor<T> extends LineProcessor <T> {

    void setCraftingMaterialMap(Map<Integer, TradeSkillMasterItem> craftingMaterialMap);
}
