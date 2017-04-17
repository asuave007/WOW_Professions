package com.srobinson24.sandbox.tradeskillmaster.dao.impl;

import com.srobinson24.sandbox.tradeskillmaster.dao.TradeSkillMasterItemDao;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;

/**
 * Created by srobinso on 3/28/2017.
 */
public class TradeSkillMasterFileTradeSkillMasterItemDaoImplTest {
    @Test
    public void testSave1Entry() throws Exception {
        TradeSkillMasterItemDao tradeSkillMasterItemDao = new FileTradeSkillMasterItemDaoImpl();
        final String testFileName = "testFile.txt";
        ReflectionTestUtils.setField(tradeSkillMasterItemDao,"fileName", testFileName);

        TradeSkillMasterItem testTradeSkillMasterItem = new TradeSkillMasterItem();
        testTradeSkillMasterItem.setName("Enchant Neck - Mark of the Trained Soldier");
        testTradeSkillMasterItem.setId(141909);
        testTradeSkillMasterItem.setRawMinBuyout(109970000);

        tradeSkillMasterItemDao.save(testTradeSkillMasterItem); //if not thrown, then we good

        File file = new File(testFileName);
        file.deleteOnExit();
    }

    @Test
    public void testSave2Entries() throws Exception {
        TradeSkillMasterItemDao tradeSkillMasterItemDao = new FileTradeSkillMasterItemDaoImpl();
        final String testFileName = "testFile.txt";
        ReflectionTestUtils.setField(tradeSkillMasterItemDao,"fileName", testFileName);

        TradeSkillMasterItem testItem1 = new TradeSkillMasterItem();
        testItem1.setName("Enchant Neck - Mark of the Trained Soldier");
        testItem1.setId(141909);
        testItem1.setRawMinBuyout(109970000);

        TradeSkillMasterItem testItem2 = new TradeSkillMasterItem();
        testItem2.setName("foo");
        testItem2.setId(190000);
        testItem2.setRawMinBuyout(170000);

        tradeSkillMasterItemDao.save(testItem1); //if not thrown, then we good
        tradeSkillMasterItemDao.save(testItem2); //if not thrown, then we good

        File file = new File(testFileName);
        file.deleteOnExit();
    }

    @Test
    public void testRead1Item() throws Exception {
        TradeSkillMasterItemDao tradeSkillMasterItemDao = new FileTradeSkillMasterItemDaoImpl();
        final String testFileName = "testFile.txt";
        ReflectionTestUtils.setField(tradeSkillMasterItemDao,"fileName", testFileName);

        TradeSkillMasterItem testTradeSkillMasterItem = new TradeSkillMasterItem();
        testTradeSkillMasterItem.setName("Enchant Neck - Mark of the Trained Soldier");
        testTradeSkillMasterItem.setId(141909);
        testTradeSkillMasterItem.setRawMinBuyout(109970000);

        tradeSkillMasterItemDao.save(testTradeSkillMasterItem); //if not thrown, then we good

        final TradeSkillMasterItem readTradeSkillMasterItem = tradeSkillMasterItemDao.read(testTradeSkillMasterItem.getId());

        Assert.assertEquals(testTradeSkillMasterItem, readTradeSkillMasterItem);

        File file = new File(testFileName);
        file.deleteOnExit();

    }

    @Test
    public void testSave2Read1Item() throws Exception {
        TradeSkillMasterItemDao tradeSkillMasterItemDao = new FileTradeSkillMasterItemDaoImpl();
        final String testFileName = "testFile.txt";
        ReflectionTestUtils.setField(tradeSkillMasterItemDao,"fileName", testFileName);

        TradeSkillMasterItem testItem1 = new TradeSkillMasterItem();
        testItem1.setName("Enchant Neck - Mark of the Trained Soldier");
        testItem1.setId(141909);
        testItem1.setRawMinBuyout(109970000);

        TradeSkillMasterItem testItem2 = new TradeSkillMasterItem();
        testItem2.setName("foo");
        testItem2.setId(190000);
        testItem2.setRawMinBuyout(170000);

        tradeSkillMasterItemDao.save(testItem1); //if not thrown, then we good
        tradeSkillMasterItemDao.save(testItem2); //if not thrown, then we good

        final TradeSkillMasterItem actualTsmItem = tradeSkillMasterItemDao.read(testItem1.getId());
        Assert.assertEquals(testItem1, actualTsmItem);

        File file = new File(testFileName);
        file.deleteOnExit();

    }

    @Test
    public void delete() throws Exception {

    }

}