package com.srobinson24.sandbox.tradeskillmaster.processor.impl;

import com.srobinson24.sandbox.tradeskillmaster.domain.Enchant;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.processor.ProfitProcessor;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Created by srobinso on 4/3/2017.
 */
public class ProfitProcessorImplTest {
    @Test
    public void testGetCraftingCost() throws Exception {
        Enchant markOfTheTrainedSoldier = new Enchant();

        final TradeSkillMasterItem chaosCrystal = new TradeSkillMasterItem();
        chaosCrystal.setId(124442);
        chaosCrystal.setRawMarketValue(3992500);

        final TradeSkillMasterItem leyLightShard = new TradeSkillMasterItem();
        leyLightShard.setId(124441);
        leyLightShard.setRawMarketValue(979000);

        final TradeSkillMasterItem arkhana = new TradeSkillMasterItem();
        arkhana.setId(124440);
        arkhana.setRawMarketValue(355000);

        markOfTheTrainedSoldier.addCraftingMaterial(chaosCrystal, 12d);
        markOfTheTrainedSoldier.addCraftingMaterial(leyLightShard, 10d);
        markOfTheTrainedSoldier.addCraftingMaterial(arkhana, 0d);

        final long actual = new ProfitProcessorImpl().getCraftingCost(markOfTheTrainedSoldier);

        Assert.assertEquals(57700000, actual);

    }

    @Test
    public void testDetermineProfit() throws Exception {

        Enchant markOfTheTrainedSoldier = new Enchant();
        markOfTheTrainedSoldier.setRawMinBuyout(99970000);

        final TradeSkillMasterItem chaosCrystal = new TradeSkillMasterItem();
        chaosCrystal.setId(124442);
        chaosCrystal.setRawMarketValue(3990000);

        final TradeSkillMasterItem leyLightShard = new TradeSkillMasterItem();
        leyLightShard.setId(124441);
        leyLightShard.setRawMarketValue(940000);

        markOfTheTrainedSoldier.addCraftingMaterial(chaosCrystal, 12d);
        markOfTheTrainedSoldier.addCraftingMaterial(leyLightShard, 10d);


        final ProfitProcessorImpl profitProcessorImpl = new ProfitProcessorImpl();
        ReflectionTestUtils.setField(profitProcessorImpl, "auctionHousePercent", 0.05);

        final long actual = profitProcessorImpl.calculateProfit(markOfTheTrainedSoldier);

        Assert.assertEquals(37691500, actual);

    }

    @Test
    public void testTruncateInsignificantDigitsRoundUp() throws Exception {
        final ProfitProcessor ProfitProcessor = new ProfitProcessorImpl();
        final long actual = ProfitProcessor.truncateSilverAndCopper(407441);
        Assert.assertEquals(41, actual);
    }

    @Test
    public void testTruncateInsignificantDigitsRoundDown() throws Exception {
        final long actual = new ProfitProcessorImpl().truncateSilverAndCopper(404441);
        Assert.assertEquals(40, actual);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testTruncateInsignificantDigitsThrowsException() throws Exception {
        new ProfitProcessorImpl().truncateSilverAndCopper(123);
    }

    @Test
    public void testGetCraftingCost2() {
        Enchant markOfTheTrainedSoldier = new Enchant();

        final TradeSkillMasterItem chaosCrystal = new TradeSkillMasterItem();
        chaosCrystal.setId(124442);
        chaosCrystal.setRawMarketValue(2732500);

        final TradeSkillMasterItem leyLightShard = new TradeSkillMasterItem();
        leyLightShard.setId(124441);
        leyLightShard.setRawMarketValue(996998);

        markOfTheTrainedSoldier.addCraftingMaterial(chaosCrystal, 12d);
        markOfTheTrainedSoldier.addCraftingMaterial(leyLightShard, 10d);

        final long actual = new ProfitProcessorImpl().getCraftingCost(markOfTheTrainedSoldier);

        Assert.assertEquals(42759980, actual);

    }

    @Test
    public void testGetCraftingCostOfDecimalValue() {
        Enchant coastalHealingPotion = new Enchant();

        final TradeSkillMasterItem pollen = new TradeSkillMasterItem();
        pollen.setId(152509);
        pollen.setRawMarketValue(37);

        coastalHealingPotion.addCraftingMaterial(pollen, 1.5d);

        final long actual = new ProfitProcessorImpl().getCraftingCost(coastalHealingPotion);

        Assert.assertEquals(55, actual);

    }

    @Test
    public void testFlaskCraftingCost() {
        Enchant flask = new Enchant();

        final TradeSkillMasterItem anchorWeed = new TradeSkillMasterItem();
        anchorWeed.setId(0);
        anchorWeed.setRawMarketValue(446);

        final TradeSkillMasterItem bite = new TradeSkillMasterItem();
        bite.setId(1);
        bite.setRawMarketValue(35);

        final TradeSkillMasterItem stalk = new TradeSkillMasterItem();
        stalk.setId(2);
        stalk.setRawMarketValue(45);

        flask.addCraftingMaterial(anchorWeed, 3.75d);
        flask.addCraftingMaterial(bite, 7.5d);
        flask.addCraftingMaterial(stalk, 11.25d);

        final long actual = new ProfitProcessorImpl().getCraftingCost(flask);

        Assert.assertEquals(2440, actual);

    }

}