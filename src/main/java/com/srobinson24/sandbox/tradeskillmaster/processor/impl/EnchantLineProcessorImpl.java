package com.srobinson24.sandbox.tradeskillmaster.processor.impl;

import com.google.common.base.Preconditions;
import com.srobinson24.sandbox.tradeskillmaster.domain.Enchant;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.processor.EnchantLineProcessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class EnchantLineProcessorImpl implements EnchantLineProcessor <Set<Enchant>> {

    private Logger logger = LoggerFactory.getLogger(EnchantLineProcessorImpl.class);
    private Set<Enchant> set = new HashSet<>();
    private Map<Integer,TradeSkillMasterItem> craftingMaterialMap;

    @Override
    public boolean processLine(String line) throws IOException {
        Preconditions.checkArgument(craftingMaterialMap != null, "Crafting materials have not been set, cannot load enchants from file!");
        Preconditions.checkNotNull(line);
        logger.debug("Processing line: {}", line);
        if (line.startsWith("#") || line.isEmpty()) {
            logger.debug("line was empty or comment: [{}]", line);
            return true; // if a comment line, just return
        }
        final String[] strings = StringUtils.split(line, ",");
        Preconditions.checkArgument(strings != null, "Line parsed to null!");
        Preconditions.checkArgument(strings.length >= 4, "Invalid line:" + line);

        processEnchant(strings);

        return true;
    }

    private void processEnchant(String[] strings) {
        final Enchant enchant = new Enchant();
        enchant.setQuantityOnhand(Integer.parseInt(strings[0].trim()));
        enchant.setId(Integer.parseInt(strings[1].trim()));
        enchant.setName(strings[2].trim());

        for (int ii = 3; ii < strings.length; ii++) {

            final String craftingMatString = strings[ii];
            final String[] craftingMats = craftingMatString.trim().split("-");
            Preconditions.checkArgument(2 == craftingMats.length, "Invalid crafting material:" + craftingMatString);
            final String craftingMatId = craftingMats[0];
            final String craftingMatQuantity = craftingMats[1];

            final Integer intCraftingMatId = Integer.valueOf(craftingMatId);
            final TradeSkillMasterItem craftingMaterial = craftingMaterialMap.get(intCraftingMatId);

            final Integer quantity = Integer.valueOf(craftingMatQuantity);
            enchant.addCraftingMaterial(craftingMaterial, quantity);

        }

        set.add(enchant);
    }

    @Override
    public Set<Enchant> getResult() {
        return set;
    }


    @Override
    public void setCraftingMaterialMap(Map<Integer, TradeSkillMasterItem> craftingMaterialMap) {
        this.craftingMaterialMap = craftingMaterialMap;
    }
}
