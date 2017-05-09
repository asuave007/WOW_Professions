package com.srobinson24.sandbox.tradeskillmaster.processor.impl;

import com.google.common.collect.ImmutableMap;
import com.srobinson24.sandbox.tradeskillmaster.domain.Enchant;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by srobinso on 4/17/2017.
 */
public class CraftingMaterialLineProcessorImplTest {

    @Test
    public void testProcessLineHappyPath() throws Exception {
        final String line1 = "124440,Arkhana,Enchant";
        final String line2 = "124441,Leylight Shard,Enchant";
        final String line3 = "124442,Chaos Crystal,Enchant";

        CraftingMaterialLineProcessorImpl craftingMaterialLineProcessor = new CraftingMaterialLineProcessorImpl();
        final boolean line1Results = craftingMaterialLineProcessor.processLine(line1);
        Assert.assertTrue(line1Results);
        final boolean line2Results = craftingMaterialLineProcessor.processLine(line2);
        Assert.assertTrue(line2Results);
        final boolean line3Results = craftingMaterialLineProcessor.processLine(line3);
        Assert.assertTrue(line3Results);

    }


    @Test (expected = IllegalArgumentException.class)
    public void testProcessLineSadPath() throws Exception {
        final String string = "1,128548,Enchant Cloak - Binding of Strength*****,124442-8,124440-30";

        CraftingMaterialLineProcessorImpl craftingMaterialLineProcessor = new CraftingMaterialLineProcessorImpl();
        craftingMaterialLineProcessor.processLine(string); // this should throw
    }

    @Test
    public void testGetResult() throws Exception {
        final String line1 = "124440,Arkhana,Enchant";
        final String line2 = "124441,Leylight Shard,Enchant";
        final String line3 = "124442,Chaos Crystal,Enchant";

        CraftingMaterialLineProcessorImpl craftingMaterialLineProcessor = new CraftingMaterialLineProcessorImpl();
        final boolean line1Results = craftingMaterialLineProcessor.processLine(line1);
        Assert.assertTrue(line1Results);
        final boolean line2Results = craftingMaterialLineProcessor.processLine(line2);
        Assert.assertTrue(line2Results);
        final boolean line3Results = craftingMaterialLineProcessor.processLine(line3);
        Assert.assertTrue(line3Results);

        final Map<Integer, TradeSkillMasterItem> actual = craftingMaterialLineProcessor.getResult();

        final Enchant arkhana = new Enchant();
        arkhana.setId(124440);
        final Enchant leylightShards = new Enchant();
        leylightShards.setId(124441);
        final Enchant crystals = new Enchant();
        crystals.setId(124442);
        final ImmutableMap<Integer, Enchant> expected =
                ImmutableMap.of(
                        arkhana.getId(), arkhana,
                        leylightShards.getId(), leylightShards,
                        crystals.getId(), crystals);

        Assert.assertEquals(expected, actual);

    }

}