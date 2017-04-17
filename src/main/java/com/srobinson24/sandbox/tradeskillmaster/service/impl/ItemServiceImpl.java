package com.srobinson24.sandbox.tradeskillmaster.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.srobinson24.sandbox.tradeskillmaster.dao.TradeSkillMasterItemDao;
import com.srobinson24.sandbox.tradeskillmaster.domain.Enchant;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.exception.RuntimeFileException;
import com.srobinson24.sandbox.tradeskillmaster.exception.RuntimeHttpException;
import com.srobinson24.sandbox.tradeskillmaster.processor.impl.EnchantLineProcessorImpl;
import com.srobinson24.sandbox.tradeskillmaster.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by srobinso on 3/24/2017.
 */
@Service
public class ItemServiceImpl implements ItemService {

    private final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final TradeSkillMasterItemDao tradeSkillMasterItemDao;

    @Value("${crafting.material.ids}")
    private Set<Integer> craftingMaterialIds;

    @Value ("${url}")
    private String url;

    @Value("${enchant.file}")
    private String enchantFile;

    @Value("${crafting.materials.file}")
    private String craftingMaterialFilePath;

    @Autowired
    @Qualifier("enchantLineProcessorImpl")
    private EnchantLineProcessorImpl enchantingLineProcessor;

    @Autowired
    @Qualifier("craftingMaterialLineProcessorImpl")
    private LineProcessor<Map<Integer,TradeSkillMasterItem>> craftingMaterialsLineProcessor;

    private final Map<Integer, TradeSkillMasterItem> craftingMaterialsMap = new HashMap<>();

    @Autowired
    public ItemServiceImpl(TradeSkillMasterItemDao tradeSkillMasterItemDao) {
        this.tradeSkillMasterItemDao = tradeSkillMasterItemDao;
    }

    @Override
    public Map<Integer, TradeSkillMasterItem> readCraftingItems() {
        final File craftingMaterialsFile = new File(craftingMaterialFilePath);
        logger.debug("file location: {}", craftingMaterialsFile.getAbsolutePath());
        try {
            return Files.readLines(craftingMaterialsFile, Charsets.UTF_8, craftingMaterialsLineProcessor);
        } catch (IOException ex) {
            throw new RuntimeFileException(ex);
        }
    }

    @Override
    public Set<Enchant> readEnchants(final Map <Integer, TradeSkillMasterItem> craftingMaterialsMap){
        final File enchantsFile = new File(enchantFile);
        logger.debug("file location: {}", enchantsFile.getAbsolutePath());
        enchantingLineProcessor.setCraftingMaterialMap(craftingMaterialsMap);
        final Set<Enchant> enchants;
        try {
            enchants = Files.readLines(enchantsFile, Charsets.UTF_8, enchantingLineProcessor);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return enchants;
    }

    @Override
    public Map<Integer, TradeSkillMasterItem> updateItemInformation2(final Set <TradeSkillMasterItem> allItems) {
        logger.debug("retrieve all items of: {}", allItems);
        final Set<Integer> itemIdsToUpdate = findItemsToUpdate(allItems);
        callUpdateService(allItems, itemIdsToUpdate);
        logger.debug("done updating, saving, {}", allItems);
        tradeSkillMasterItemDao.saveAll(allItems);
        logger.debug("done saving, reading all from disk");
        final Map<Integer, TradeSkillMasterItem> tsmItemMap = tradeSkillMasterItemDao.readAll();
        return tsmItemMap;
    }

    private Set<Integer> findItemsToUpdate2(Set<TradeSkillMasterItem> enchants) {
        final Set <Integer> itemsToUpdate = Sets.newHashSet();
        for (TradeSkillMasterItem enchant : enchants) {
            final TradeSkillMasterItem tsmItem = tradeSkillMasterItemDao.read(enchant.getId());
            if (tsmItem == null || tsmItem.getLastUpdate() == null) itemsToUpdate.add(enchant.getId());
            else if (tsmItem.getLastUpdate().plusHours(1).isBefore(LocalDateTime.now())) itemsToUpdate.add(enchant.getId());
            else logger.debug("Pulling item from disk: {}", tsmItem);
        }

        return itemsToUpdate;
    }

    private Set<Integer> findItemsToUpdate(Set<TradeSkillMasterItem> enchantsFromFile) {
        final Set <Integer> itemsToUpdate = Sets.newHashSet();
        for (TradeSkillMasterItem enchant : enchantsFromFile) {
            final TradeSkillMasterItem itemFromDisk = tradeSkillMasterItemDao.read(enchant.getId());
            if (itemFromDisk == null || itemFromDisk.getLastUpdate() == null) itemsToUpdate.add(enchant.getId());
            else if (itemFromDisk.getLastUpdate().plusHours(1).isBefore(LocalDateTime.now())) itemsToUpdate.add(enchant.getId());
            else {
                logger.debug("Pulling item from disk: {}", itemFromDisk);
                enchant.setLastUpdate(itemFromDisk.getLastUpdate());
                enchant.setRawMinBuyout(itemFromDisk.getRawMinBuyout());
                enchant.setRawMarketValue(itemFromDisk.getRawMarketValue());
            }
        }

        return itemsToUpdate;
    }

    private TradeSkillMasterItem processHttpRequest(final String urlWithId) throws IOException {
        logger.info("About to call url: {}", urlWithId);
        final HttpResponse response = Request.Get(urlWithId).addHeader("Accept", "application/json").execute().returnResponse();
        logger.debug("Response code is: {}", response.getStatusLine().getStatusCode());

        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            final String responseString = entity != null ? EntityUtils.toString(entity) : null;
            return processObject(responseString);
        }
        else {
            throw new RuntimeHttpException("Invalid HTTP response code: " + status);
        }
    }

    private TradeSkillMasterItem processObject (final String responseContent) {
        logger.debug("Converting Http Response to Java Object: {}", responseContent);
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        try {
            return objectMapper.readValue(responseContent, TradeSkillMasterItem.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    private Set <TradeSkillMasterItem> callTSMService(final Set<Integer> itemIdsToUpdate) {

        logger.info("updating from tsm: {}", itemIdsToUpdate);
        final Set <TradeSkillMasterItem> updatedItems = Sets.newHashSet();

        for (Integer id : itemIdsToUpdate) {
            final String urlWithId = StringUtils.replace(url, "{$id}", id.toString());
            try {
                final TradeSkillMasterItem tradeSkillMasterItem = processHttpRequest(urlWithId);
                tradeSkillMasterItem.setLastUpdate(LocalDateTime.now());
                updatedItems.add(tradeSkillMasterItem);
            } catch (IOException ex) {
                throw new RuntimeHttpException(ex);
            }

        }


        return updatedItems;

    }

    @Override
    public void callUpdateService(final Set<TradeSkillMasterItem> itemsToUpdate, Set<Integer> itemIdsToUpdate) {

        logger.info("updating from tsm: {}", itemsToUpdate);

        itemsToUpdate.stream().filter(item -> itemIdsToUpdate.contains(item.getId())).forEach(item -> {
            final String id = String.valueOf(item.getId());
            final String urlWithId = StringUtils.replace(url, "{$id}", id);
            try {
                final TradeSkillMasterItem updatedItem = processHttpRequest(urlWithId);
                item.setLastUpdate(LocalDateTime.now());
                item.setRawMinBuyout(updatedItem.getRawMinBuyout());
                item.setRawMarketValue(updatedItem.getRawMarketValue());
                item.setNumberOfAuctions(updatedItem.getNumberOfAuctions());
            } catch (IOException ex) {
                throw new RuntimeHttpException(ex);
            }

        });

    }

}
