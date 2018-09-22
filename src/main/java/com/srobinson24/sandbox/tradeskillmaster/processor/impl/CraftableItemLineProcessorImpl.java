package com.srobinson24.sandbox.tradeskillmaster.processor.impl;

import com.google.common.base.Preconditions;
import com.srobinson24.sandbox.tradeskillmaster.domain.*;
import com.srobinson24.sandbox.tradeskillmaster.processor.CraftableItemLineProcessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
//todo: need to modify the actual line processing to depend on the item type as an argument - ex. pigments different from inks from alchemy
public class CraftableItemLineProcessorImpl implements CraftableItemLineProcessor<Set<CraftableItem>> {

    private final Logger logger = LoggerFactory.getLogger(CraftableItemLineProcessorImpl.class);
    private final Set<CraftableItem> set = new HashSet<>();
    private Map<Integer,TradeSkillMasterItem> craftingMaterialMap;

    @Override
    public boolean processLine(String line) throws IOException {
        Preconditions.checkArgument(craftingMaterialMap != null, "Crafting materials have not been set, cannot load crafts from file!");
        Preconditions.checkNotNull(line);
        logger.debug("Processing line: {}", line);
        if (line.startsWith("#") || line.isEmpty()) {
            logger.debug("line was empty or comment: [{}]", line);
            return true; // if a comment line, just return
        }
        final String[] strings = StringUtils.split(line, ",");
        Preconditions.checkArgument(strings != null, "Line parsed to null!");
        Preconditions.checkArgument(strings.length >= 4, "Invalid line:" + line);

        parseItem(strings);

        return true;
    }

    private void parseItem(String[] strings) {
        if ("Pigment".equalsIgnoreCase(strings[0])) set.add(parseInscriptionMaterials(strings));
        else if ("Ink".equalsIgnoreCase(strings[0])) set.add(parseInscriptionMaterials(strings));
        else if ("Inscription".equalsIgnoreCase(strings[0])) set.add(parseInscriptionMaterials(strings));
        else set.add(parseMaterials(strings));
    }

    private CraftableItem parseInscriptionMaterials(String[] strings) {
        if (strings[0].trim().equalsIgnoreCase("Inscription")) return  parseInscriptionCraft(strings);
        if (strings[0].trim().equalsIgnoreCase("Pigment")) return parsePigment(strings);
        else if (strings[0].trim().equalsIgnoreCase("Ink")) return parseInk(strings);
        else throw new IllegalArgumentException("Invalid Argument.  Must be Pigment or Ink but was: " + strings[4]);
    }

    private CraftableItem parseInscriptionCraft(String[] strings) {

        CraftableItem item = new CraftableItem();
        item.setQuantityOnhand(Integer.parseInt(strings[1].trim()));
        item.setQuantityDesired(Integer.parseInt(strings[2].trim()));
        item.setId(Integer.parseInt(strings[3].trim()));
        item.setName(strings[4].trim());
        final String[] mats = strings[5].trim().split("-");
        final Integer inkId = Integer.valueOf(mats[0]);
        final double quantity = Double.parseDouble(mats[1]);

        final TradeSkillMasterItem ink = craftingMaterialMap.get(inkId);
        item.addCraftingMaterial(ink, quantity);

        item.setCraftingType(CraftingType.INSCRIPTION);

        return item;


    }

    private CraftableItem parseInk(String[] strings) {

        InscriptionInk ink = new InscriptionInk();

        ink.setQuantityOnhand(Integer.parseInt(strings[1].trim()));
        ink.setId(Integer.parseInt(strings[2].trim()));
        ink.setName(strings[3].trim());

        final String[] mats = strings[4].trim().split("-");

        final Integer pigmentId = Integer.valueOf(mats[0]);
        final double quantity = Double.parseDouble(mats[1]);
        final TradeSkillMasterItem pigment = craftingMaterialMap.get(pigmentId);

        ink.addCraftingMaterial(pigment, quantity);

        ink.setCraftingType(CraftingType.INK);

        return ink;

    }

    private CraftableItem parsePigment(String[] strings) {
        InscriptionPigment pigment = new InscriptionPigment();
        pigment.setQuantityOnhand(Integer.parseInt(strings[1].trim()));
        pigment.setId(Integer.parseInt(strings[2].trim()));
        pigment.setName(strings[3].trim());
        for (int ii = 4; ii < strings.length; ii++) {

            final String string = strings[ii];
            final String[] matsRate = string.trim().split("-");
            Preconditions.checkArgument(2 == matsRate.length, "Invalid crafting material:" + string);
            final String craftingMatId = matsRate[0];
            final String craftingMatRate = matsRate[1];

            final Integer intCraftingMatId = Integer.valueOf(craftingMatId);
            final TradeSkillMasterItem herb = craftingMaterialMap.get(intCraftingMatId);

            final Double rate = Double.valueOf(craftingMatRate);
            pigment.addHerbMillingRate(herb, rate);
        }
        pigment.setCraftingType(CraftingType.PIGMENT);
        return pigment;

    }

    private CraftableItem parseMaterials(String[] strings) {
        final CraftableItem craftableItem = new CraftableItem();
        craftableItem.setCraftingType(CraftingType.valueOf(strings[0].trim()));
        craftableItem.setQuantityOnhand(Integer.parseInt(strings[1].trim()));
        craftableItem.setQuantityDesired(Integer.parseInt(strings[2].trim()));
        craftableItem.setId(Integer.parseInt(strings[3].trim()));
        craftableItem.setName(strings[4].trim());
        for (int ii = 5; ii < strings.length; ii++) {

            final String craftingMatString = strings[ii];
            final String[] craftingMats = craftingMatString.trim().split("-");
            Preconditions.checkArgument(2 == craftingMats.length, "Invalid crafting material:" + craftingMatString);
            final String craftingMatId = craftingMats[0];
            final String craftingMatQuantity = craftingMats[1];

            final Integer intCraftingMatId = Integer.valueOf(craftingMatId);
            final TradeSkillMasterItem craftingMaterial = craftingMaterialMap.get(intCraftingMatId);

            final Double quantity = Double.valueOf(craftingMatQuantity);
            craftableItem.addCraftingMaterial(craftingMaterial, quantity);

        }

        return craftableItem;
    }

    @Override
    public Set<CraftableItem> getResult() {
        return set;
    }


    @Override
    public void setCraftingMaterialMap(Map<Integer, TradeSkillMasterItem> craftingMaterialMap) {
        this.craftingMaterialMap = craftingMaterialMap;
    }
}
