package com.srobinson24.sandbox.tradeskillmaster.processor.impl;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.srobinson24.sandbox.tradeskillmaster.domain.CraftableItem;
import com.srobinson24.sandbox.tradeskillmaster.domain.CraftingType;
import com.srobinson24.sandbox.tradeskillmaster.domain.InscriptionPigment;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by srobinso on 4/11/2017.
 */
public class CraftableItemLineProcessorImplTest {

    @Test
    public void testProcessLineWithQuantity60() throws Exception {
        final String string = "0,60,152494,Coastal Healing Potion,152509-1.41";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        lineProcessor.setCraftingMaterialMap(new HashMap<>());
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
        final CraftableItem craftableItem = lineProcessor.getResult().stream().findFirst().get();
        final int actual = craftableItem.getQuantityDesired();
        Assert.assertEquals(60, actual);
    }

    @Test
    public void testProcessLineWithDecimal() throws Exception {
        final String string = "0,60,152494,Coastal Healing Potion,152509-1.5";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        lineProcessor.setCraftingMaterialMap(new HashMap<>());
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }

    @Test
    public void testProcessLineHappyPath() throws Exception {
        final String string = "0,15,152638,Flask of the Currents,152510-3.52,152511-7.04,152507-5.63";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        lineProcessor.setCraftingMaterialMap(new HashMap<>());
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }

    @Test
    public void testProcessLineHappyPathWith0Materials() throws Exception {
        final String string = "0,15,152638,Flask of the Currents,152510-3.52,152511-0,152507-5.63";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        final HashMap<Integer, TradeSkillMasterItem> craftingMaterialMap = new HashMap<>();
        final TradeSkillMasterItem craftingMat1 = new TradeSkillMasterItem();
        craftingMat1.setId(152510);
        final TradeSkillMasterItem craftingMat2 = new TradeSkillMasterItem();
        craftingMat2.setId(152511);
        final TradeSkillMasterItem craftingMat3 = new TradeSkillMasterItem();
        craftingMat3.setId(152507);
        craftingMaterialMap.put(craftingMat1.getId(), craftingMat1);
        craftingMaterialMap.put(craftingMat2.getId(), craftingMat2);
        craftingMaterialMap.put(craftingMat3.getId(), craftingMat3);
        lineProcessor.setCraftingMaterialMap(craftingMaterialMap);
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
        final CraftableItem firstCraftableItem = lineProcessor.getResult().iterator().next();
        final Map<TradeSkillMasterItem, Double> craftingMaterials = firstCraftableItem.getCraftingMaterials();
        Assert.assertTrue(craftingMaterials.containsValue(0.0));
    }

    @Test
    public void testCommentLine() throws Exception {
        final String string = "#neck enchants";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        lineProcessor.setCraftingMaterialMap(new HashMap<>());
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }

    @Test
    public void testEmptyLine() throws Exception {
        final String string = "";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        lineProcessor.setCraftingMaterialMap(new HashMap<>());
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }



    @Test
    public void testInk() throws Exception {
        final String string = "0,15,158187,Ultramarine Ink,Inscription, Ink,153635-1";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        lineProcessor.setCraftingMaterialMap(new HashMap<>());
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }


    @Test
    public void testPigment() throws Exception {
        final String string = "0,15,153635,Ultramarine Pigment,Inscription,Pigment,152505-1.21,152506-1.21,152507-1.21,152508-1.21,152509-1.21,152510-1.21,152511-1.21";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        lineProcessor.setCraftingMaterialMap(new HashMap<>());
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);

        final Set<CraftableItem> result = lineProcessor.getResult();
        Assert.assertEquals(1,result.size());
        final CraftableItem craftableItem = result.iterator().next();
        Assert.assertTrue(craftableItem instanceof InscriptionPigment);
        Assert.assertEquals(CraftingType.INSCRIPTION_PIGMENT, craftableItem.getCraftingType());

    }


}