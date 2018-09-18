package com.srobinson24.sandbox.tradeskillmaster.processor.impl;

import com.google.common.collect.ImmutableMap;
import com.srobinson24.sandbox.tradeskillmaster.domain.CraftableItem;
import com.srobinson24.sandbox.tradeskillmaster.domain.CraftingType;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by srobinso on 4/17/2017.
 */
public class CraftingMaterialLineProcessorImplTest {

    @Test
    public void testProcessLineHappyPath() throws Exception {
        final String line1 = "124440,Arkhana";
        final String line2 = "124441,Leylight Shard";
        final String line3 = "124442,Chaos Crystal";

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
        final String string = "1,128548,CraftableItem Cloak - Binding of Strength*****,124442-8,124440-30";

        CraftingMaterialLineProcessorImpl craftingMaterialLineProcessor = new CraftingMaterialLineProcessorImpl();
        craftingMaterialLineProcessor.processLine(string); // this should throw
    }

    @Test
    public void testGetResult() throws Exception {
        final String line1 = "124440,Arkhana";
        final String line2 = "124441,Leylight Shard";
        final String line3 = "124442,Chaos Crystal";

        CraftingMaterialLineProcessorImpl craftingMaterialLineProcessor = new CraftingMaterialLineProcessorImpl();
        final boolean line1Results = craftingMaterialLineProcessor.processLine(line1);
        Assert.assertTrue(line1Results);
        final boolean line2Results = craftingMaterialLineProcessor.processLine(line2);
        Assert.assertTrue(line2Results);
        final boolean line3Results = craftingMaterialLineProcessor.processLine(line3);
        Assert.assertTrue(line3Results);

        final Map<Integer, TradeSkillMasterItem> actual = craftingMaterialLineProcessor.getResult();

        Assert.assertEquals(3, actual.size());

    }

    @Test
    public void testInscriptionPigments () throws IOException {
        final String string = "153635,Ultramarine Pigment";
        CraftingMaterialLineProcessorImpl lineProcessor = new CraftingMaterialLineProcessorImpl();
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }

    @Test
    public void testInscriptionInks() throws IOException {
        final String string = "158188,Crimson Ink";
        CraftingMaterialLineProcessorImpl lineProcessor = new CraftingMaterialLineProcessorImpl();
        final boolean continueProcessing = lineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }

}