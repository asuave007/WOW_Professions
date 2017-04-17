package com.srobinson24.sandbox.tradeskillmaster.dao;

import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;

import java.util.Map;
import java.util.Set;

/**
 * Created by srobinso on 3/28/2017.
 */
public interface TradeSkillMasterItemDao {

    boolean save(TradeSkillMasterItem tradeSkillMasterItem);
    boolean saveAll(Set<TradeSkillMasterItem> items);
    TradeSkillMasterItem read (Integer id);
    Map<Integer, TradeSkillMasterItem> readAll ();
    boolean delete (TradeSkillMasterItem tradeSkillMasterItem);


}
