package com.srobinson24.sandbox.tradeskillmaster.service.impl;

import com.google.common.collect.Sets;
import com.srobinson24.sandbox.tradeskillmaster.domain.CraftableItem;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class PricingServiceImplTest {

    @Test
    public void calculateMats() {
        final PricingServiceImpl pricingService = new PricingServiceImpl(null, null);

        final TradeSkillMasterItem weed = new TradeSkillMasterItem(1, "weed");
        final TradeSkillMasterItem stalk = new TradeSkillMasterItem(2, "stalk");
        final TradeSkillMasterItem bite = new TradeSkillMasterItem(3, "bite");
        final TradeSkillMasterItem bud = new TradeSkillMasterItem(4, "bud");
        final TradeSkillMasterItem pollen = new TradeSkillMasterItem(5, "pollen");
        final TradeSkillMasterItem kiss = new TradeSkillMasterItem(6, "kiss");


        final CraftableItem flaskOfTheCurrents = new CraftableItem("currents");
        flaskOfTheCurrents.setQuantityDesired(15);
        flaskOfTheCurrents.addCraftingMaterial(weed, 3.52);
        flaskOfTheCurrents.addCraftingMaterial(bite, 7.14);
        flaskOfTheCurrents.addCraftingMaterial(stalk, 10.71);

        final CraftableItem flaskOfFathoms = new CraftableItem("fathoms");
        flaskOfFathoms.setQuantityDesired(15);
        flaskOfFathoms.addCraftingMaterial(weed, 3.52);
        flaskOfFathoms.addCraftingMaterial(kiss, 7.14);
        flaskOfFathoms.addCraftingMaterial(bud, 10.71);

        final CraftableItem battlePotOfAgility = new CraftableItem("agility");
        battlePotOfAgility.setQuantityDesired(60);
        battlePotOfAgility.addCraftingMaterial(bud, 5.71);
        battlePotOfAgility.addCraftingMaterial(pollen, 7.14);

        final Map<TradeSkillMasterItem, Double> tradeSkillMasterItemDoubleMap = pricingService.calculateMats(Sets.newHashSet(flaskOfTheCurrents, flaskOfFathoms, battlePotOfAgility));

        Assert.assertEquals(105.6, tradeSkillMasterItemDoubleMap.get(weed), 0.01);
        Assert.assertEquals(107.1, tradeSkillMasterItemDoubleMap.get(bite), 0.01);
        Assert.assertEquals(428.4, tradeSkillMasterItemDoubleMap.get(pollen), 0.01);
        Assert.assertEquals(503.25, tradeSkillMasterItemDoubleMap.get(bud), 0.01);
        Assert.assertEquals(107.1, tradeSkillMasterItemDoubleMap.get(kiss), 0.01);
        Assert.assertEquals(160.65, tradeSkillMasterItemDoubleMap.get(stalk), 0.01);


    }
}