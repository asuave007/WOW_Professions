package com.srobinson24.sandbox.tradeskillmaster.dao.impl;

import com.google.common.collect.ImmutableMap;
import com.srobinson24.sandbox.tradeskillmaster.dao.TradeSkillMasterItemDao;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.Map;

/**
 * Created by srobinso on 3/28/2017.
 */
public class TradeSkillMasterFileDaoImplTest {

    @Rule
    public final TemporaryFolder tempFile = new TemporaryFolder();

    @Test
    public void testSave1Entry() throws Exception {


        TradeSkillMasterItemDao tradeSkillMasterItemDao = new FileTradeSkillMasterItemDaoImpl(tempFile.newFile("testFile.txt"));

        TradeSkillMasterItem testTradeSkillMasterItem = new TradeSkillMasterItem();
        testTradeSkillMasterItem.setName("CraftableItem Neck - Mark of the Trained Soldier");
        testTradeSkillMasterItem.setId(141909);
        testTradeSkillMasterItem.setRawMinBuyout(109970000);

        tradeSkillMasterItemDao.saveAll(ImmutableMap.of(testTradeSkillMasterItem.getId(), testTradeSkillMasterItem)); //if not thrown, then we good

    }

    @Test
    public void testSave2Entries() throws Exception {
        TradeSkillMasterItemDao tradeSkillMasterItemDao = new FileTradeSkillMasterItemDaoImpl(tempFile.newFile("testFile.txt"));

        TradeSkillMasterItem testItem1 = new TradeSkillMasterItem();
        testItem1.setName("CraftableItem Neck - Mark of the Trained Soldier");
        testItem1.setId(141909);
        testItem1.setRawMinBuyout(109970000);

        TradeSkillMasterItem testItem2 = new TradeSkillMasterItem();
        testItem2.setName("foo");
        testItem2.setId(190000);
        testItem2.setRawMinBuyout(170000);

        tradeSkillMasterItemDao.saveAll(ImmutableMap.of(testItem1.getId(), testItem1, testItem2.getId(), testItem2)); //if not thrown, then we good

    }

    @Test
    public void testSave1Read1Item() throws Exception {
        TradeSkillMasterItemDao tradeSkillMasterItemDao = new FileTradeSkillMasterItemDaoImpl(tempFile.newFile("testFile.txt"));

        TradeSkillMasterItem testTradeSkillMasterItem = new TradeSkillMasterItem();
        testTradeSkillMasterItem.setName("CraftableItem Neck - Mark of the Trained Soldier");
        testTradeSkillMasterItem.setId(141909);
        testTradeSkillMasterItem.setRawMinBuyout(109970000);

        tradeSkillMasterItemDao.saveAll(ImmutableMap.of(testTradeSkillMasterItem.getId(), testTradeSkillMasterItem)); //if not thrown, then we good

        final Map<Integer, TradeSkillMasterItem> itemMap = tradeSkillMasterItemDao.readAll();

        final TradeSkillMasterItem tsmItem = itemMap.get(testTradeSkillMasterItem.getId());

        Assert.assertEquals(testTradeSkillMasterItem, tsmItem);


    }

    @Test
    public void testSave2Read1Item() throws Exception {
        TradeSkillMasterItemDao tradeSkillMasterItemDao = new FileTradeSkillMasterItemDaoImpl(tempFile.newFile("testFile.txt"));

        TradeSkillMasterItem testItem1 = new TradeSkillMasterItem();
        testItem1.setName("CraftableItem Neck - Mark of the Trained Soldier");
        testItem1.setId(141909);
        testItem1.setRawMinBuyout(109970000);

        TradeSkillMasterItem testItem2 = new TradeSkillMasterItem();
        testItem2.setName("foo");
        testItem2.setId(190000);
        testItem2.setRawMinBuyout(170000);

        tradeSkillMasterItemDao.saveAll(ImmutableMap.of(testItem1.getId(), testItem1, testItem2.getId(), testItem2)); //if not thrown, then we good

        final Map<Integer, TradeSkillMasterItem> allItems = tradeSkillMasterItemDao.readAll();
        Assert.assertEquals(testItem1, allItems.get(testItem1.getId()));


    }

}