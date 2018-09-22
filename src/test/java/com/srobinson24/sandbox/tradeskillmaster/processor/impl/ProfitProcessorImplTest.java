package com.srobinson24.sandbox.tradeskillmaster.processor.impl;

import com.srobinson24.sandbox.tradeskillmaster.domain.*;
import com.srobinson24.sandbox.tradeskillmaster.processor.ProfitProcessor;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Created by srobinso on 4/3/2017.
 */
public class ProfitProcessorImplTest {


    @Test
    public void testGetCraftingCostInkCheaperToBuy() {
        final InscriptionPigment crimsonPigment = new InscriptionPigment("Crimson Pigment");
        crimsonPigment.setCraftingType(CraftingType.PIGMENT);
        crimsonPigment.setRawMinBuyout(22);

        //todo: not correct!!!! should value the cost of the pigments!!!!
        final TradeSkillMasterItem herb5 = new TradeSkillMasterItem("Riverbud", 1, 48);
        final TradeSkillMasterItem herb6 = new TradeSkillMasterItem("Star Moss", 2, 51);

        crimsonPigment.addHerbMillingRate(herb5, 3.16);
        crimsonPigment.addHerbMillingRate(herb6, 3.16);

        final InscriptionInk ink = new InscriptionInk("Crimson Ink");
        ink.setCraftingType(CraftingType.INK);

        ink.addCraftingMaterial(crimsonPigment, 1.0);
        ink.setRawMinBuyout(22);

        final ProfitProcessorImpl profitProcessor = new ProfitProcessorImpl();
        final double actual = profitProcessor.getCraftingCost(ink);

        Assert.assertEquals(22, actual, 0.05);
        Assert.assertFalse(ink.isCheaperToCraft());
    }

    @Test
    public void testGetCraftingCostInkCraftedIsCheaperThanBought() {
        InscriptionPigment crimsonPigment = new InscriptionPigment("Crimson Pigment");
        crimsonPigment.setCraftingType(CraftingType.PIGMENT);
        crimsonPigment.setRawMinBuyout(158900);

        final TradeSkillMasterItem herb5 = new TradeSkillMasterItem("Riverbud", 152505, 290000);
        final TradeSkillMasterItem herb6 = new TradeSkillMasterItem("Star Moss", 152506, 300000);

        crimsonPigment.addHerbMillingRate(herb5, 3.16);
        crimsonPigment.addHerbMillingRate(herb6, 3.16);

        InscriptionInk ink = new InscriptionInk("Crimson Ink");
        ink.setCraftingType(CraftingType.INK);

        ink.addCraftingMaterial(crimsonPigment, 1.0);
        ink.setRawMinBuyout(199999);

        final ProfitProcessorImpl profitProcessor = new ProfitProcessorImpl();
        final double actual = profitProcessor.getCraftingCost(ink);

        Assert.assertEquals(158900, actual, 0.05);
        Assert.assertTrue(ink.isCheaperToCraft());
    }

    @Test
    public void testGetCraftingCostPigmentIsCheaperToBuy() {
        final InscriptionPigment crimsonPigment = new InscriptionPigment("Crimson Pigment");
        crimsonPigment.setCraftingType(CraftingType.PIGMENT);
        crimsonPigment.setRawMinBuyout(121);

        final TradeSkillMasterItem herb5 = new TradeSkillMasterItem("Riverbud", 1, 48);
        final TradeSkillMasterItem herb6 = new TradeSkillMasterItem("Star Moss", 2, 51);

        crimsonPigment.addHerbMillingRate(herb5, 3.16);
        crimsonPigment.addHerbMillingRate(herb6, 3.16);

        final ProfitProcessorImpl profitProcessor = new ProfitProcessorImpl();
        final double actual = profitProcessor.getCraftingCost(crimsonPigment);

        Assert.assertEquals(121, actual, 0.05);
        Assert.assertFalse(crimsonPigment.isCheaperToCraft());
    }

    @Test
    public void testGetCraftingCostPigmentIsCheaperToCraft() {
        final InscriptionPigment crimsonPigment = new InscriptionPigment("Crimson Pigment");
        crimsonPigment.setCraftingType(CraftingType.PIGMENT);
        crimsonPigment.setRawMinBuyout(121);

        final TradeSkillMasterItem herb5 = new TradeSkillMasterItem("Riverbud", 1, 25);
        final TradeSkillMasterItem herb6 = new TradeSkillMasterItem("Star Moss", 2, 36);

        crimsonPigment.addHerbMillingRate(herb5, 3.16);
        crimsonPigment.addHerbMillingRate(herb6, 3.16);

        final ProfitProcessorImpl profitProcessor = new ProfitProcessorImpl();
        final double actual = profitProcessor.getCraftingCost(crimsonPigment);

        Assert.assertEquals(79, actual, 0.05);
        Assert.assertTrue(crimsonPigment.isCheaperToCraft());
    }

    @Test
    public void testGetCraftingCostPigmentCheaperToCraftViridescentPigment() {
        InscriptionPigment virisdescentPigment = new InscriptionPigment();
        virisdescentPigment.setCraftingType(CraftingType.PIGMENT);

        final TradeSkillMasterItem bud = new TradeSkillMasterItem("Riverbud", 152505, 5);
        final TradeSkillMasterItem moss = new TradeSkillMasterItem("Star Moss", 152506, 6);
        final TradeSkillMasterItem bite = new TradeSkillMasterItem("Akunda's Bite", 152507, 7);
        final TradeSkillMasterItem kiss = new TradeSkillMasterItem("Winter's Kiss", 152508, 8);
        final TradeSkillMasterItem pollen = new TradeSkillMasterItem("Siren's Pollen", 152509, 9);
        final TradeSkillMasterItem weed = new TradeSkillMasterItem("Anchor Weed", 1525010, 10);
        final TradeSkillMasterItem stalk = new TradeSkillMasterItem("Sea Stalk", 1525011, 11);

        virisdescentPigment.addHerbMillingRate(bud, 7.58);
        virisdescentPigment.addHerbMillingRate(moss, 7.58);
        virisdescentPigment.addHerbMillingRate(bite, 7.58);
        virisdescentPigment.addHerbMillingRate(kiss, 7.58);
        virisdescentPigment.addHerbMillingRate(pollen, 7.58);
        virisdescentPigment.addHerbMillingRate(weed, 3.05);
        virisdescentPigment.addHerbMillingRate(stalk, 7.58);

        final double actual = new ProfitProcessorImpl().getCraftingCost(virisdescentPigment);

        Assert.assertEquals(3.05 * 10, actual, 0.05);

    }

    @Test
    public void testGetCraftingCostPigmentCheaperToCraftCrimsonPigment() {
        InscriptionPigment crimsonPigment = new InscriptionPigment();
        crimsonPigment.setCraftingType(CraftingType.PIGMENT);

        final TradeSkillMasterItem herb5 = new TradeSkillMasterItem("Riverbud", 152505, 5);
        final TradeSkillMasterItem herb6 = new TradeSkillMasterItem("Star Moss", 152506, 6);
        final TradeSkillMasterItem herb7 = new TradeSkillMasterItem("Akunda's Bite", 152507, 7);
        final TradeSkillMasterItem herb8 = new TradeSkillMasterItem("Winter's Kiss", 152508, 8);
        final TradeSkillMasterItem herb9 = new TradeSkillMasterItem("Siren's Pollen", 152509, 9);
        final TradeSkillMasterItem herb10 = new TradeSkillMasterItem("Anchor Weed", 1525010, 10);
        final TradeSkillMasterItem herb11 = new TradeSkillMasterItem("Sea Stalk", 1525011, 11);

        crimsonPigment.addHerbMillingRate(herb5, 3.16);
        crimsonPigment.addHerbMillingRate(herb6, 3.16);
        crimsonPigment.addHerbMillingRate(herb7, 3.16);
        crimsonPigment.addHerbMillingRate(herb8, 3.16);
        crimsonPigment.addHerbMillingRate(herb9, 3.16);
        crimsonPigment.addHerbMillingRate(herb10, 3.16);
        crimsonPigment.addHerbMillingRate(herb11, 3.16);

        final double actual = new ProfitProcessorImpl().getCraftingCost(crimsonPigment);

        Assert.assertEquals(3.16 * 5, actual, 0.05);

    }

    @Test
    public void testGetCraftingCostPigmentCheaperToCraft()  {
        InscriptionPigment pigment = new InscriptionPigment();
        pigment.setCraftingType(CraftingType.PIGMENT);

        final TradeSkillMasterItem herb5 = new TradeSkillMasterItem("Riverbud", 152505, 5);
        final TradeSkillMasterItem herb6 = new TradeSkillMasterItem("Star Moss", 152506, 6);
        final TradeSkillMasterItem herb7 = new TradeSkillMasterItem("Akunda's Bite", 152507, 7);
        final TradeSkillMasterItem herb8 = new TradeSkillMasterItem("Winter's Kiss", 152508, 8);
        final TradeSkillMasterItem herb9 = new TradeSkillMasterItem("Siren's Pollen", 152509, 9);
        final TradeSkillMasterItem herb10 = new TradeSkillMasterItem("Anchor Weed", 1525010, 10);
        final TradeSkillMasterItem herb11 = new TradeSkillMasterItem("Sea Stalk", 1525011, 11);

        pigment.addHerbMillingRate(herb5, 1.21);
        pigment.addHerbMillingRate(herb6, 1.21);
        pigment.addHerbMillingRate(herb7, 1.21);
        pigment.addHerbMillingRate(herb8, 1.21);
        pigment.addHerbMillingRate(herb9, 1.21);
        pigment.addHerbMillingRate(herb10, 1.21);
        pigment.addHerbMillingRate(herb11, 1.21);

        final double actual = new ProfitProcessorImpl().getCraftingCost(pigment);

        Assert.assertEquals(1.21 * 5, actual, 0.05);

    }
    
    @Test
    public void testGetCraftingCost() throws Exception {
        CraftableItem markOfTheTrainedSoldier = new CraftableItem();

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

        final double actual = new ProfitProcessorImpl().getCraftingCost(markOfTheTrainedSoldier);

        Assert.assertEquals(57700000, actual, 0.05);

    }

    @Test
    public void testDetermineProfitWith1Auction() throws Exception {

        CraftableItem markOfTheTrainedSoldier = new CraftableItem();
        markOfTheTrainedSoldier.setRawMinBuyout(99970000);
        markOfTheTrainedSoldier.setNumberOfAuctions(1);
        markOfTheTrainedSoldier.setRawMarketValue(37691500);

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

        final double actual = profitProcessorImpl.calculateProfit(markOfTheTrainedSoldier);

        Assert.assertEquals(37691500, actual,0.05);

    }

    @Test
    public void testDetermineProfitWithNoCompetingAuctions() throws Exception {

        CraftableItem markOfTheTrainedSoldier = new CraftableItem();
        markOfTheTrainedSoldier.setName("Mark of the Trained Soilder");
        markOfTheTrainedSoldier.setRawMinBuyout(99970000);
        markOfTheTrainedSoldier.setNumberOfAuctions(0);
        markOfTheTrainedSoldier.setRawMarketValue(37691500);

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

        final double actual = profitProcessorImpl.calculateProfit(markOfTheTrainedSoldier);

        Assert.assertEquals(-17892383, actual, 0.5);

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
        CraftableItem markOfTheTrainedSoldier = new CraftableItem();

        final TradeSkillMasterItem chaosCrystal = new TradeSkillMasterItem();
        chaosCrystal.setId(124442);
        chaosCrystal.setRawMarketValue(2732500);

        final TradeSkillMasterItem leyLightShard = new TradeSkillMasterItem();
        leyLightShard.setId(124441);
        leyLightShard.setRawMarketValue(996998);

        markOfTheTrainedSoldier.addCraftingMaterial(chaosCrystal, 12d);
        markOfTheTrainedSoldier.addCraftingMaterial(leyLightShard, 10d);

        final double actual = new ProfitProcessorImpl().getCraftingCost(markOfTheTrainedSoldier);

        Assert.assertEquals(42759980, actual,0.05);

    }

    @Test
    public void testGetCraftingCostOfDecimalValue() {
        CraftableItem coastalHealingPotion = new CraftableItem();

        final TradeSkillMasterItem pollen = new TradeSkillMasterItem();
        pollen.setId(152509);
        pollen.setRawMarketValue(37);

        coastalHealingPotion.addCraftingMaterial(pollen, 1.5d);

        final double actual = new ProfitProcessorImpl().getCraftingCost(coastalHealingPotion);

        Assert.assertEquals(55, actual,0.05);

    }

    @Test
    public void testFlaskCraftingCost() {
        CraftableItem flask = new CraftableItem();

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

        final double actual = new ProfitProcessorImpl().getCraftingCost(flask);

        Assert.assertEquals(2440, actual, 0.05);

    }

    @Test
    public void testProfit() {
        CraftableItem craftableItem = new CraftableItem();
        craftableItem.setRawMarketValue(20);


        final TradeSkillMasterItem craftingMaterial = new TradeSkillMasterItem(0);
        craftingMaterial.setRawMarketValue(17);
        craftableItem.addCraftingMaterial(craftingMaterial,1.0);

        ProfitProcessorImpl profitProcessor = new ProfitProcessorImpl();
        final double actualProfit = profitProcessor.calculateProfit(craftableItem);
        final long expectedProfit = 22-17;
        Assert.assertEquals(actualProfit, expectedProfit,0.05);
    }

}