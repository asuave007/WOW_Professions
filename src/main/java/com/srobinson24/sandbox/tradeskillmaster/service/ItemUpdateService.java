package com.srobinson24.sandbox.tradeskillmaster.service;

import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;

/**
 * Created by srobinso on 4/18/2017.
 */
public interface ItemUpdateService {

    TradeSkillMasterItem fetchUpdateItemInformation(int id);
}
