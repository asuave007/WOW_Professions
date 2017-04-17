package com.srobinson24.sandbox.tradeskillmaster.processor;

import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.processor.impl.EnchantLineProcessorImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by srobinso on 4/11/2017.
 */
public class EnchantLineProcessorImplTest {

    @Test
    public void testProcessLineHappyPath() throws Exception {
        final String string = "1,128548,Enchant Cloak - Binding of Strength*****,124442-8,124440-30";

        EnchantLineProcessorImpl enchantLineProcessor = new EnchantLineProcessorImpl();
        final boolean continueProcessing = enchantLineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }

    @Test
    public void testProcessLineHappyPathWithOMaterials() throws Exception {
        final String string = "1,128548,Enchant Cloak - Binding of Strength*****,124442-8,124441-0,124440-30";

        EnchantLineProcessorImpl enchantLineProcessor = new EnchantLineProcessorImpl();
        final boolean continueProcessing = enchantLineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
        final Map<TradeSkillMasterItem, Integer> craftingMaterials = enchantLineProcessor.getResult().iterator().next().getCraftingMaterials();
        Assert.assertTrue(craftingMaterials.containsValue(0));
    }

    @Test
    public void testCommentLine() throws Exception {
        final String string = "#neck enchants";

        EnchantLineProcessorImpl enchantLineProcessor = new EnchantLineProcessorImpl();
        final boolean continueProcessing = enchantLineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }

    @Test
    public void testEmptyLine() throws Exception {
        final String string = "";

        EnchantLineProcessorImpl enchantLineProcessor = new EnchantLineProcessorImpl();
        final boolean continueProcessing = enchantLineProcessor.processLine(string);
        Assert.assertTrue(continueProcessing);
    }


}