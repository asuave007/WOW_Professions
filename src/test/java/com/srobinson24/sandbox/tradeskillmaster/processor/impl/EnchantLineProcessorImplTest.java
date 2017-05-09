package com.srobinson24.sandbox.tradeskillmaster.processor.impl;

import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.processor.impl.EnchantLineProcessorImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by srobinso on 4/11/2017.
 */
public class EnchantLineProcessorImplTest {

    @Test
    public void testProcessLineHappyPath() throws Exception {
        final String string = "1,128548,Enchant Cloak - Binding of Strength*****,124442-8,124440-30";

        EnchantLineProcessorImpl enchantLineProcessor = new EnchantLineProcessorImpl();
        enchantLineProcessor.setCraftingMaterialMap(new HashMap<>());
        final boolean continueProcessing = enchantLineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }

    @Test
    public void testProcessLineHappyPathWithOMaterials() throws Exception {
        final String string = "1,128548,Enchant Cloak - Binding of Strength*****,124442-8,124441-0,124440-30";

        EnchantLineProcessorImpl enchantLineProcessor = new EnchantLineProcessorImpl();
        final HashMap<Integer, TradeSkillMasterItem> craftingMaterialMap = new HashMap<>();
        final TradeSkillMasterItem crystal = new TradeSkillMasterItem();
        crystal.setId(124442);
        final TradeSkillMasterItem shard = new TradeSkillMasterItem();
        shard.setId(124441);
        final TradeSkillMasterItem arkhana = new TradeSkillMasterItem();
        arkhana.setId(124440);
        craftingMaterialMap.put(crystal.getId(), crystal);
        craftingMaterialMap.put(shard.getId(), shard);
        craftingMaterialMap.put(arkhana.getId(), arkhana);
        enchantLineProcessor.setCraftingMaterialMap(craftingMaterialMap);
        final boolean continueProcessing = enchantLineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
        final Map<TradeSkillMasterItem, Integer> craftingMaterials = enchantLineProcessor.getResult().iterator().next().getCraftingMaterials();
        Assert.assertTrue(craftingMaterials.containsValue(0));
    }

    @Test
    public void testCommentLine() throws Exception {
        final String string = "#neck enchants";

        EnchantLineProcessorImpl enchantLineProcessor = new EnchantLineProcessorImpl();
        enchantLineProcessor.setCraftingMaterialMap(new HashMap<>());
        final boolean continueProcessing = enchantLineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }

    @Test
    public void testEmptyLine() throws Exception {
        final String string = "";

        EnchantLineProcessorImpl enchantLineProcessor = new EnchantLineProcessorImpl();
        enchantLineProcessor.setCraftingMaterialMap(new HashMap<>());
        final boolean continueProcessing = enchantLineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }


}