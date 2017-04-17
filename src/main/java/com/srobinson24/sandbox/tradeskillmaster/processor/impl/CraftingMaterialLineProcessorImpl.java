package com.srobinson24.sandbox.tradeskillmaster.processor.impl;

import com.google.common.base.Preconditions;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by srobinso on 3/24/2017.
 */
@Component
public class CraftingMaterialLineProcessorImpl implements com.google.common.io.LineProcessor<Map<Integer,TradeSkillMasterItem>> {

    private Logger logger = LoggerFactory.getLogger(CraftingMaterialLineProcessorImpl.class);

    private Map <Integer,TradeSkillMasterItem> map = new HashMap<>();

    public boolean processLine(String line) throws IOException {
        Preconditions.checkNotNull(line);
        if (line.startsWith("#") || line.isEmpty()) {
            logger.debug("line was empty or comment: [{}]", line);
            return true; // if a comment line, just return
        }
        final String[] strings = StringUtils.split(line, ",");
        Preconditions.checkArgument(strings != null, "Line parsed to null!");
        Preconditions.checkArgument(3 == strings.length, "Invalid line:" + line);

        final TradeSkillMasterItem craftingMaterial = new TradeSkillMasterItem();
        craftingMaterial.setId(Integer.parseInt(strings[0]));
        craftingMaterial.setName(strings[1]);
        map.put(craftingMaterial.getId(), craftingMaterial);

        return true;
    }

    public Map<Integer, TradeSkillMasterItem> getResult() {
        return map;
    }

}
