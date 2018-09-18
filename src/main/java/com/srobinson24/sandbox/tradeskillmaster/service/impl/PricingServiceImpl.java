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


    public void getPricedEnchants() {

        final Map<Integer, TradeSkillMasterItem> craftingItemsMap = itemService.readCraftingItemsFromFile();
        final Set<CraftableItem> craftableItems = itemService.readEnchantsFromFile(craftingItemsMap);

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
            totalProfit += profit;
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

        Set <CraftableItem> profitableToCraftCraftableItems = profitableCraftableItems.parallelStream()
                .filter(e -> e.getQuantityOnhand() == 0)
                .filter(e -> profitProcessor.calculateProfit(e) >= profitThreshold)
                .collect(Collectors.toSet());

        final Set<CraftableItem> notProfitableEnchantsOnHand = allCraftableItems.parallelStream()
                .filter(e -> !onHandProfitableCraftableItems.contains(e))
                .filter(e -> !profitableToCraftCraftableItems.contains(e))
                .filter(e -> e.getQuantityOnhand() > 0)
                .collect(Collectors.toSet());

        logger.info("ON HAND, NO CRAFT: {}", onHandProfitableCraftableItems.size());
        printNames (onHandProfitableCraftableItems);
        logger.info("CRAFTED: {}", profitableToCraftCraftableItems.size());
        printNames (profitableToCraftCraftableItems);
        logger.info("NOT PROFITABLE: {}", notProfitableEnchantsOnHand.size());
        printNames (notProfitableEnchantsOnHand);

        double totalCraftingCost = calculateCraftingCost (profitableToCraftCraftableItems);

        totalProfit = totalProfit / 10000;
        totalCraftingCost = totalCraftingCost / 10000;


        logger.info("Total Profit: {} Total Outlays: {}", formatter.format(totalProfit), formatter.format(totalCraftingCost));

        final Map<TradeSkillMasterItem, Double> tradeSkillMasterItemIntegerMap = calculateMats(profitableToCraftCraftableItems);
        tradeSkillMasterItemIntegerMap.forEach((item, count) -> logger.info("Total Mats: {}:{}", item.getName(), count));

    }

    //fixme: this should be placed elsewhere
    private Map<TradeSkillMasterItem, Double> calculateMats(Set<CraftableItem> profitableToCraftCraftableItems) {
        final Map<TradeSkillMasterItem, Double> totalMats = new HashMap<>();
        for (final CraftableItem profitableToCraftCraftableItem : profitableToCraftCraftableItems) {
            final Map<TradeSkillMasterItem, Double> craftingMaterials = profitableToCraftCraftableItem.getCraftingMaterials();
            for (Map.Entry<TradeSkillMasterItem, Double> entry : craftingMaterials.entrySet()) {
                final TradeSkillMasterItem tempItem = new TradeSkillMasterItem(entry.getKey().getId());
                final Double aDouble = totalMats.get(tempItem);
                int quantity = profitableToCraftCraftableItem.getName().contains("Flask") ? 11 : 60;
                if (aDouble == null) totalMats.put(entry.getKey(), entry.getValue() * quantity);
                else totalMats.put(entry.getKey(), entry.getValue() + (totalMats.get(tempItem) * quantity));
            }
        }

        return totalMats;
    }

    private double calculateCraftingCost(Set<CraftableItem> profitableToCraftCraftableItems) {
        return profitableToCraftCraftableItems.stream().mapToDouble(profitProcessor::getCraftingCost).sum();
    }

    private void printNames(Set<CraftableItem> onHandProfitableCraftableItems) {
        for (CraftableItem onHandProfitableCraftableItem : onHandProfitableCraftableItems) {
            logger.info("{}", onHandProfitableCraftableItem.getName());
        }
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
                    .filter(enchant -> profitProcessor.calculateProfit(enchant) > profitThreshold)
                    .collect(Collectors.toSet());

            sortedSet.addAll(profitableCraftableItems);
        }

        return sortedSet;

    }


}
