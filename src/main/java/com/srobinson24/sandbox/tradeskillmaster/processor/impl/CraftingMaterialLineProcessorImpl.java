package com.srobinson24.sandbox.tradeskillmaster.processor.impl;

import com.google.common.base.Preconditions;
import com.google.common.io.LineProcessor;
import com.srobinson24.sandbox.tradeskillmaster.domain.CraftingType;
import com.srobinson24.sandbox.tradeskillmaster.domain.InscriptionInk;
import com.srobinson24.sandbox.tradeskillmaster.domain.InscriptionPigment;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by srobinso on 3/24/2017.
 */
@Component
public class CraftingMaterialLineProcessorImpl implements LineProcessor<Map<Integer, TradeSkillMasterItem>> {

    private final Logger logger = LoggerFactory.getLogger(CraftingMaterialLineProcessorImpl.class);

    private final Map<Integer, TradeSkillMasterItem> map = new HashMap<>();

    public boolean processLine(String line) {
        Preconditions.checkNotNull(line);
        if (line.startsWith("#") || line.isEmpty()) {
            logger.debug("line was empty or comment: [{}]", line);
            return true; // if a comment line, just return
        }
        final String[] strings = StringUtils.split(line, ",");
        Preconditions.checkArgument(strings != null, "Line parsed to null!");
        Preconditions.checkArgument(2 == strings.length || 3 == strings.length, "Invalid line:" + line);

        if (strings.length == 3) return processInscription(strings);
        final TradeSkillMasterItem craftingMaterial = new TradeSkillMasterItem();
        craftingMaterial.setId(Integer.parseInt(strings[0]));
        craftingMaterial.setName(strings[1]);
        map.put(craftingMaterial.getId(), craftingMaterial);

        return true;
    }

    private boolean processInscription(String[] strings) {
        final TradeSkillMasterItem craftingMaterial;
        if ("pigment".equalsIgnoreCase(strings[2])) {
            craftingMaterial = new InscriptionPigment();
            craftingMaterial.setCraftingType(CraftingType.PIGMENT);
        } else if ("ink".equalsIgnoreCase(strings[2])) {
            craftingMaterial = new InscriptionInk();
            craftingMaterial.setCraftingType(CraftingType.INK);
        } else {
            throw new IllegalArgumentException("Invalid argument for line: " + strings[2]);
        }

        craftingMaterial.setId(Integer.parseInt(strings[0]));
        craftingMaterial.setName(strings[1]);
        map.put(craftingMaterial.getId(), craftingMaterial);
        return true;
    }

    public Map<Integer, TradeSkillMasterItem> getResult() {
        return map;
    }

}
