package com.srobinson24.sandbox.tradeskillmaster.processor.impl;

import com.srobinson24.sandbox.tradeskillmaster.domain.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by srobinso on 4/11/2017.
 */
public class CraftableItemLineProcessorImplTest {

    private Map<Integer, TradeSkillMasterItem> createCraftingMaterialsMap() {

        final InscriptionInk crimsonInk = new InscriptionInk("crimson ink");
        crimsonInk.setId(158188);
        final InscriptionInk ultramarineInk = new InscriptionInk("ultramarine ink");
        ultramarineInk.setId(158187);
        final TradeSkillMasterItem anchorWeed = new TradeSkillMasterItem("Anchor Weed");
        anchorWeed.setId(152510);
        final TradeSkillMasterItem seaStalk = new TradeSkillMasterItem("Sea Stalk");
        seaStalk.setId(152511);
        final TradeSkillMasterItem akundaBite = new TradeSkillMasterItem("Akunda's Bite");
        akundaBite.setId(152507);

        final Map<Integer, TradeSkillMasterItem> craftingMaterialMap = new HashMap<>();
        craftingMaterialMap.put(ultramarineInk.getId(), ultramarineInk);
        craftingMaterialMap.put(crimsonInk.getId(), crimsonInk);
        craftingMaterialMap.put(anchorWeed.getId(), anchorWeed);
        craftingMaterialMap.put(seaStalk.getId(), seaStalk);
        craftingMaterialMap.put(akundaBite.getId(), akundaBite);

        return craftingMaterialMap;

    }

    @Test
    public void testProcessLineWithInscription2Mats() throws Exception {
        final String string = "INSCRIPTION,0,40,153647,Tome of the Quiet Mind,158187-12,158188-6";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();

        lineProcessor.setCraftingMaterialMap(createCraftingMaterialsMap());

        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
        final CraftableItem craftableItem = lineProcessor.getResult().stream().findFirst().get();
        Assert.assertEquals(CraftingType.INSCRIPTION, craftableItem.getCraftingType());
        Assert.assertEquals(2, craftableItem.getCraftingMaterials().size());
    }

    @Test
    public void testProcessLineWithInscription() throws Exception {
        final String string = "INSCRIPTION,0,15,158202,War-Scroll of Battle Shout,158188-8";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        lineProcessor.setCraftingMaterialMap(Collections.emptyMap());
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
        final CraftableItem craftableItem = lineProcessor.getResult().stream().findFirst().get();
        final CraftingType actual = craftableItem.getCraftingType();
        Assert.assertEquals(CraftingType.INSCRIPTION, actual);
    }

    @Test
    public void testProcessLineWithQuantity60() throws Exception {
        final String string = "ALCHEMY,0,60,152494,Coastal Healing Potion,152509-1.41";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        lineProcessor.setCraftingMaterialMap(Collections.emptyMap());
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
        final CraftableItem craftableItem = lineProcessor.getResult().stream().findFirst().get();
        final int actual = craftableItem.getQuantityDesired();
        Assert.assertEquals(60, actual);
        Assert.assertEquals(CraftingType.ALCHEMY, craftableItem.getCraftingType());
    }

    @Test
    public void testProcessLineWithDecimal() throws Exception {
        final String string = "ALCHEMY,0,60,152494,Coastal Healing Potion,152509-1.5";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        lineProcessor.setCraftingMaterialMap(Collections.emptyMap());
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }

    @Test
    public void testProcessLineHappyPath() throws Exception {
        final String string = "ALCHEMY,0,15,152638,Flask of the Currents,152510-3.52,152511-7.04,152507-5.63";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        lineProcessor.setCraftingMaterialMap(Collections.emptyMap());
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }

    @Test
    public void testProcessLineHappyPathWith0Materials() throws Exception {
        final String string = "ALCHEMY,0,15,152638,Flask of the Currents,152510-3.52,152511-0,152507-5.63";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        lineProcessor.setCraftingMaterialMap(createCraftingMaterialsMap());
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
        lineProcessor.setCraftingMaterialMap(Collections.emptyMap());
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }

    @Test
    public void testEmptyLine() throws Exception {
        final String string = "";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        lineProcessor.setCraftingMaterialMap(Collections.emptyMap());
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }



    @Test
    public void testInk() throws Exception {
        final String string = "Ink,0,158188,Crimson Ink,153636-1";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        lineProcessor.setCraftingMaterialMap(Collections.emptyMap());
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }


    @Test
    public void testPigment() throws Exception {
        final String string = "Pigment,0,153635,Ultramarine Pigment,152505-1.21,152506-1.21,152507-1.21,152508-1.21,152509-1.21,152510-1.21,152511-1.21";

        CraftableItemLineProcessorImpl lineProcessor = new CraftableItemLineProcessorImpl();
        lineProcessor.setCraftingMaterialMap(Collections.emptyMap());
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);

        final Set<CraftableItem> result = lineProcessor.getResult();
        Assert.assertEquals(1,result.size());
        final CraftableItem craftableItem = result.iterator().next();
        Assert.assertTrue(craftableItem instanceof InscriptionPigment);
        Assert.assertEquals(CraftingType.PIGMENT, craftableItem.getCraftingType());

    }


}