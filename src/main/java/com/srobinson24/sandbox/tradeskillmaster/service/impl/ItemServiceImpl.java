package com.srobinson24.sandbox.tradeskillmaster.service.impl;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.srobinson24.sandbox.tradeskillmaster.dao.TradeSkillMasterItemDao;
import com.srobinson24.sandbox.tradeskillmaster.domain.Enchant;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.exception.RuntimeFileProcessingException;
import com.srobinson24.sandbox.tradeskillmaster.processor.EnchantLineProcessor;
import com.srobinson24.sandbox.tradeskillmaster.service.ItemService;
import com.srobinson24.sandbox.tradeskillmaster.service.ItemUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by srobinso on 3/24/2017.
 */
@Service
public class ItemServiceImpl implements ItemService {

    private final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
    @Value("${crafting.material.ids}")
    private Set<Integer> craftingMaterialIds;

    @Value("${enchant.file}")
    private String enchantFilePath;

    @Value("${crafting.materials.file}")
    private String craftingMaterialsFilePath;

    @Value("${minutes.before.update}")
    private long minutesBeforeUpdate;


    private final TradeSkillMasterItemDao tradeSkillMasterItemDao;
    private final ItemUpdateService itemUpdateService;
    private final EnchantLineProcessor <Set<Enchant>> enchantingLineProcessor;
    private final LineProcessor<Map<Integer,TradeSkillMasterItem>> craftingMaterialsLineProcessor;

    @Autowired
    public ItemServiceImpl(final TradeSkillMasterItemDao tsmItemDao,
                           final ItemUpdateService itemUpdateService,
                           final EnchantLineProcessor<Set<Enchant>> enchantLineProcessor,
                           final LineProcessor<Map<Integer, TradeSkillMasterItem>> craftingMaterialsLineProcessor) {
        this.tradeSkillMasterItemDao = tsmItemDao;
        this.itemUpdateService = itemUpdateService;
        this.enchantingLineProcessor = enchantLineProcessor;
        this.craftingMaterialsLineProcessor = craftingMaterialsLineProcessor;
    }

    @Override
    public Map<Integer, TradeSkillMasterItem> readCraftingItems() {
        final File craftingMaterialsFile = new File(craftingMaterialsFilePath);
        logger.debug("file location: {}", craftingMaterialsFile.getAbsolutePath());
        try {
            return Files.readLines(craftingMaterialsFile, Charsets.UTF_8, craftingMaterialsLineProcessor);
        } catch (IOException ex) {
            throw new RuntimeFileProcessingException(ex);
        }
    }

    @Override
    public Set<Enchant> readEnchants(final Map <Integer, TradeSkillMasterItem> craftingMaterialsKeyedOnId){
        final File enchantsFile = new File(enchantFilePath);
        logger.debug("file location: {}", enchantsFile.getAbsolutePath());
        enchantingLineProcessor.setCraftingMaterialMap(craftingMaterialsKeyedOnId);
        final Set<Enchant> enchants;
        try {
            enchants = Files.readLines(enchantsFile, Charsets.UTF_8, enchantingLineProcessor);
        } catch (IOException ex) {
            throw new RuntimeFileProcessingException(ex);
        }
        return enchants;
    }

    @Override
    public Map<Integer, TradeSkillMasterItem> updateItemInformation(final Set <TradeSkillMasterItem> allItems) {
        logger.debug("updating item information on items: {}", allItems);
        updateFromCache(allItems);
        final Set<TradeSkillMasterItem> itemsToUpdate = findItemsToUpdate(allItems);
        callUpdateService(itemsToUpdate);
        tradeSkillMasterItemDao.saveAll(allItems);
        return tradeSkillMasterItemDao.readAll();
    }

    public Set <TradeSkillMasterItem> findItemsToUpdate(Set<TradeSkillMasterItem> allItems) {
        return allItems.stream()
                .filter(item -> Objects.isNull(item.getLastUpdate()) ||
                    LocalDateTime.now().minusMinutes(minutesBeforeUpdate).isAfter(item.getLastUpdate()))
                .collect(Collectors.toSet());
    }

    @Override
    public void updateFromCache(Set<TradeSkillMasterItem> enchantsFromFile) {
        for (TradeSkillMasterItem enchant : enchantsFromFile) {
            final TradeSkillMasterItem itemFromDisk = tradeSkillMasterItemDao.read(enchant.getId());
            if (itemFromDisk != null
                    && itemFromDisk.getLastUpdate() != null
                    && !itemFromDisk.getLastUpdate().plusMinutes(minutesBeforeUpdate).isBefore(LocalDateTime.now())) {
                        logger.debug("Pulling item from disk: {}", itemFromDisk);

                        BeanUtils.copyProperties(itemFromDisk,enchant);

                    } else {
                logger.debug("Adding item to list of items to update: {}", itemFromDisk);
            }
        }
    }

    @Override
    public void callUpdateService(final Set<TradeSkillMasterItem> itemsToUpdate) {

        logger.info("updating from tsm: {}", itemsToUpdate);

        itemsToUpdate.forEach(item -> {

            final TradeSkillMasterItem updatedTsmItem = itemUpdateService.fetchUpdateItemInformation(item.getId());
            BeanUtils.copyProperties(updatedTsmItem, item);
            item.setLastUpdate(LocalDateTime.now());
        });

    }

}
