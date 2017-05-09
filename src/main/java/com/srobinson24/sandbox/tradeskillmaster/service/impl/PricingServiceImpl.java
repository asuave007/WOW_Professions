package com.srobinson24.sandbox.tradeskillmaster.service.impl;

import com.google.common.base.Preconditions;
import com.srobinson24.sandbox.tradeskillmaster.domain.Enchant;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.processor.ProfitProcessor;
import com.srobinson24.sandbox.tradeskillmaster.service.ItemService;
import com.srobinson24.sandbox.tradeskillmaster.service.PricingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
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
    public PricingServiceImpl(final ItemService itemService,
                              final ProfitProcessor ProfitProcessor) {
        this.itemService = itemService;
        this.profitProcessor = ProfitProcessor;
    }


    public void getPricedEnchants() {

        final Map<Integer, TradeSkillMasterItem> craftingItemsMap = itemService.readCraftingItems();
        final Set<Enchant> enchants = itemService.readEnchants(craftingItemsMap);

        final Set<TradeSkillMasterItem> allItems = Stream.concat(enchants.stream(), craftingItemsMap.values().stream()).collect(Collectors.toSet());
        logger.debug("Items to update: {}", allItems);
        final Map<Integer, TradeSkillMasterItem> itemMap = itemService.updateItemInformation(allItems);

        final Set<Enchant> profitableEnchants = sortByProfit(enchants);
        displayOutput(profitableEnchants, craftingItemsMap, enchants);

    }

    private void displayOutput(Set<Enchant> profitableEnchants, Map<Integer, TradeSkillMasterItem> craftingItems, Set<Enchant> allEnchants) {
        logger.info("*******************");
        logger.info("**FINAL SOLUTIONS**");
        if (printAll) logger.info("Printing Everything");
        else logger.info("Printing everything about profit threshold {} gold", profitThreshold);
//        int totalCraftingCost = 0;
        int totalProfit = 0;

        final DecimalFormat formatter = new DecimalFormat("###,###");
        //todo: delete this loop
        for (Enchant e : profitableEnchants) {
            final int craftingCost = profitProcessor.getCraftingCost(e);
            final Integer profit = profitProcessor.calculateProfit(e);
            totalProfit += profit;
            logger.info("Profit: [{}] Sales Price: [{}] Crafting Cost: [{}] Name: [{}] ",
                    String.format("%6s", (formatter.format(profit))),
                    String.format("%6s", formatter.format(e.getRawMinBuyout())),
                    String.format("%6s", formatter.format(craftingCost)),
                    e.getName());
        }

        Set<Enchant> onHandProfitableEnchants = profitableEnchants.parallelStream()
                .filter(e -> e.getQuantityOnhand() > 0)
                .filter(e -> profitProcessor.calculateProfit(e) >= profitThreshold)
                .collect(Collectors.toSet());

        Set <Enchant> profitableToCraftEnchants = profitableEnchants.parallelStream()
                .filter(e -> e.getQuantityOnhand() == 0)
                .filter(e -> profitProcessor.calculateProfit(e) >= profitThreshold)
                .collect(Collectors.toSet());

        final Set<Enchant> notProfitableEnchantsOnHand = allEnchants.parallelStream()
                .filter(e -> !onHandProfitableEnchants.contains(e))
                .filter(e -> !profitableToCraftEnchants.contains(e))
                .filter(e -> e.getQuantityOnhand() > 0)
                .collect(Collectors.toSet());

        logger.info("ON HAND, NO CRAFT: {}", onHandProfitableEnchants.size());
        printNames (onHandProfitableEnchants);
        logger.info("CRAFTED: {}", profitableToCraftEnchants.size());
        printNames (profitableToCraftEnchants);
        logger.info("NOT PROFITABLE: {}", notProfitableEnchantsOnHand.size());
        printNames (notProfitableEnchantsOnHand);

        int totalCraftingCost = calculateCraftingCost (profitableToCraftEnchants);


        logger.info("Total Profit: {} Total Outlays: {}", formatter.format(totalProfit), formatter.format(totalCraftingCost));
//
//        for (Map.Entry<Integer, TradeSkillMasterItem> tsmItemEntry : craftingItems.entrySet()) {
//            if (Integer.valueOf(124440).equals(tsmItemEntry.getKey())) arkhanaAvailable = tsmItemEntry.getValue().getNumberOfAuctions();
//            else if (Integer.valueOf(124441).equals(tsmItemEntry.getKey())) shardsAvailable = tsmItemEntry.getValue().getNumberOfAuctions();
//            else if (Integer.valueOf(124442).equals(tsmItemEntry.getKey())) crystalsAvailable = tsmItemEntry.getValue().getNumberOfAuctions(); // 124442
//            else throw new RuntimeException("Shit Dun Broke");
//        }
//
//
//        crystals = profitableToCraftEnchants.parallelStream().mapToInt(Enchant::getChaosCrystalsRequired).sum();
//        shards = profitableToCraftEnchants.parallelStream().mapToInt(Enchant::getLeylightShardsRequired).sum();
//        arkhana = profitableToCraftEnchants.parallelStream().mapToInt(Enchant::getArkhanaRequired).sum();
//
//        logger.info("Needed/Quantity for Sale - Crystals: {}/{} Shards: {}/{} Arkhana: {}/{}", crystals, crystalsAvailable, shards, shardsAvailable, arkhana, arkhanaAvailable);

    }

    private int calculateCraftingCost(Set<Enchant> profitableToCraftEnchants) {
        return profitableToCraftEnchants.stream().mapToInt(profitProcessor::getCraftingCost).sum();
    }

    private void printNames(Set<Enchant> onHandProfitableEnchants) {
        for (Enchant onHandProfitableEnchant : onHandProfitableEnchants) {
            logger.info("{}", onHandProfitableEnchant.getName());
        }
    }
//
//    @Override
//    public SortedSet<Enchant> sortByProfit(Set<Enchant> enchants) {
//        final TreeSet<Enchant> sortedSet = new TreeSet<>((o1, o2) -> {
//            Preconditions.checkNotNull(o1);
//            Preconditions.checkNotNull(o2);
//            return profitProcessor.calculateProfit(o2).compareTo(profitProcessor.calculateProfit(o1));
//        });
//
//        if (printAll) {
//            sortedSet.addAll(enchants);
//        } else {
//            final Set<Enchant> profitableEnchants = enchants
//                    .stream()
//                    .filter(enchant -> profitProcessor.calculateProfit(enchant) > profitThreshold)
//                    .collect(Collectors.toSet());
//
//            sortedSet.addAll(profitableEnchants);
//        }
//
//        return sortedSet;
//
//    }


    @Override
    public SortedSet<Enchant> sortByProfit(Set<Enchant> enchants) {
        logger.debug("sorting enchants by profit: {}", enchants);
        final TreeSet<Enchant> sortedSet = new TreeSet<>((o1, o2) -> {
            Preconditions.checkNotNull(o1);
            Preconditions.checkNotNull(o2);
            return Integer.compare(profitProcessor.calculateProfit(o2), profitProcessor.calculateProfit(o1));
        });

        if (printAll) {
            sortedSet.addAll(enchants);
        } else {
            final Set<Enchant> profitableEnchants = enchants
                    .stream()
                    .filter(enchant -> profitProcessor.calculateProfit(enchant) > profitThreshold)
                    .collect(Collectors.toSet());

            sortedSet.addAll(profitableEnchants);
        }

        return sortedSet;

    }


}
