package com.srobinson24.sandbox.tradeskillmaster.service.impl;

import com.google.common.base.Preconditions;
import com.srobinson24.sandbox.tradeskillmaster.domain.CraftableItem;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.processor.ProfitProcessor;
import com.srobinson24.sandbox.tradeskillmaster.service.ItemService;
import com.srobinson24.sandbox.tradeskillmaster.service.PricingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PricingServiceImpl implements PricingService {

    private final Logger logger = LoggerFactory.getLogger(PricingServiceImpl.class);

    @Value("${pricing.threshold}")
    private int profitThreshold;

    @Value("${print.all}")
    private boolean printAll;

    private final ItemService itemService;
    private final ProfitProcessor profitProcessor;

    @Autowired
    public PricingServiceImpl(final ItemService itemService, final ProfitProcessor ProfitProcessor) {
        this.itemService = itemService;
        this.profitProcessor = ProfitProcessor;
    }


    public void getPricedCrafts() {

        final Map<Integer, TradeSkillMasterItem> craftingItemsMap = itemService.readCraftingMaterialsFromFile();
        final Set<CraftableItem> craftableItems = itemService.readCraftsFromFile(craftingItemsMap);

        final Set<TradeSkillMasterItem> allItems = Stream.concat(craftableItems.stream(), craftingItemsMap.values().stream()).collect(Collectors.toSet());
        logger.debug("Items to update: {}", allItems);
        itemService.updateItemInformation(allItems);

        final Set<CraftableItem> profitableCraftableItems = sortByProfit(craftableItems);
        displayOutput(profitableCraftableItems, craftingItemsMap, craftableItems);

    }

    private void displayOutput(Set<CraftableItem> profitableCraftableItems, Map<Integer, TradeSkillMasterItem> craftingItems, Set<CraftableItem> allCraftableItems) {
        logger.info("*******************");
        logger.info("**FINAL SOLUTIONS**");
        if (printAll) logger.info("Printing Everything");
        else logger.info("Printing everything about profit threshold {} gold", profitThreshold / 10000);
        int totalProfit = 0;

        final DecimalFormat formatter = new DecimalFormat("###,###");
        //todo: delete this loop
        for (CraftableItem e : profitableCraftableItems) {
            final double craftingCost = profitProcessor.getCraftingCost(e);
            final double profit = profitProcessor.calculateProfit(e);
            totalProfit += profit * e.getQuantityDesired();
            logger.info("Profit: [{}] Sales Price: [{}] Crafting Cost: [{}] Name: [{}] ",
                    String.format("%6s", (formatter.format(profit / 10000))),
                    String.format("%6s", formatter.format(profitProcessor.getLowestSalePrice(e) / 10000)),
                    String.format("%6s", formatter.format(craftingCost / 10000)),
                    e.getName());
        }

        Set<CraftableItem> onHandProfitableCraftableItems = profitableCraftableItems.parallelStream()
                .filter(e -> e.getQuantityOnhand() > 0)
                .filter(e -> profitProcessor.calculateProfit(e) >= profitThreshold)
                .collect(Collectors.toSet());

        Set<CraftableItem> profitableToCraftCraftableItems = profitableCraftableItems.parallelStream()
                .filter(e -> e.getQuantityOnhand() == 0)
                .filter(e -> profitProcessor.calculateProfit(e) >= profitThreshold)
                .collect(Collectors.toSet());

        final Set<CraftableItem> notProfitableCraftsOnHand = allCraftableItems.parallelStream()
                .filter(e -> !onHandProfitableCraftableItems.contains(e))
                .filter(e -> !profitableToCraftCraftableItems.contains(e))
                .filter(e -> e.getQuantityOnhand() > 0)
                .collect(Collectors.toSet());

        logger.info("ON HAND, NO CRAFT: {}", onHandProfitableCraftableItems.size());
        printNames(onHandProfitableCraftableItems);
        logger.info("CRAFTED: {}", profitableToCraftCraftableItems.size());
        printNames(profitableToCraftCraftableItems);
        logger.info("NOT PROFITABLE: {}", notProfitableCraftsOnHand.size());
        printNames(notProfitableCraftsOnHand);

        double totalCraftingCost = calculateCraftingCost(profitableToCraftCraftableItems);

        totalProfit = totalProfit / 10000;
        totalCraftingCost = totalCraftingCost / 10000;


        logger.info("Total Profit: {} Total Outlays: {}", formatter.format(totalProfit), formatter.format(totalCraftingCost));

        final Map<TradeSkillMasterItem, Double> tradeSkillMasterItemIntegerMap = calculateMats(profitableToCraftCraftableItems);
        tradeSkillMasterItemIntegerMap.forEach((item, count) -> logger.info("Total Mats: {}:{}", item.getName(), (int) Math.ceil(count)));

    }

    //fixme: this should be placed elsewhere
    @Override
    public Map<TradeSkillMasterItem, Double> calculateMats(Set<CraftableItem> profitableItems) {
        final Map<TradeSkillMasterItem, Double> totalMats = new HashMap<>();
        for (final CraftableItem profitableItem : profitableItems) {
            for (Map.Entry<TradeSkillMasterItem, Double> craftingMaterial : profitableItem.getCraftingMaterials().entrySet()) {
                final TradeSkillMasterItem item = craftingMaterial.getKey();
                final Double runningTotalOfMats = totalMats.get(item);
                int totalToCraft = profitableItem.getQuantityDesired();
                final Double neededForOne = craftingMaterial.getValue();
                if (runningTotalOfMats == null) totalMats.put(item, neededForOne * totalToCraft);
                else totalMats.put(item, neededForOne * totalToCraft + runningTotalOfMats);
            }
        }

        return totalMats;
    }

    private double calculateCraftingCost(Set<CraftableItem> profitableToCraftCraftableItems) {
        return profitableToCraftCraftableItems.stream().mapToDouble(craftableItem -> profitProcessor.getCraftingCost(craftableItem) * craftableItem.getQuantityDesired()).sum();
    }

    private void printNames(Set<CraftableItem> onHandProfitableCraftableItems) {
        onHandProfitableCraftableItems.forEach(item -> logger.info(" {} of {}, if alchemy, craft {}", item.getQuantityDesired(), item.getName(), item.getQuantityDesired() / 1.41));
//        for (CraftableItem onHandProfitableCraftableItem : onHandProfitableCraftableItems) {
//            logger.info("{}", onHandProfitableCraftableItem.getName());
//        }
    }


    @Override
    public SortedSet<CraftableItem> sortByProfit(Set<CraftableItem> craftableItems) {
        logger.debug("sorting craftableItems by profit: {}", craftableItems);
        final TreeSet<CraftableItem> sortedSet = new TreeSet<>((o1, o2) -> {
            Preconditions.checkNotNull(o1);
            Preconditions.checkNotNull(o2);
            return Double.compare(profitProcessor.calculateProfit(o2), profitProcessor.calculateProfit(o1));
        });

        if (printAll) {
            sortedSet.addAll(craftableItems);
        } else {
            final Set<CraftableItem> profitableCraftableItems = craftableItems
                    .stream()
                    .filter(craft -> profitProcessor.calculateProfit(craft) > profitThreshold)
                    .collect(Collectors.toSet());

            sortedSet.addAll(profitableCraftableItems);
        }

        return sortedSet;

    }


}
