package com.srobinson24.sandbox.tradeskillmaster.dao;

import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;

import java.util.Map;

/**
 * Created by srobinso on 3/28/2017.
 */
public interface TradeSkillMasterItemDao {

    void saveAll(Map<Integer, TradeSkillMasterItem> map);
    Map<Integer, TradeSkillMasterItem> readAll ();

}
