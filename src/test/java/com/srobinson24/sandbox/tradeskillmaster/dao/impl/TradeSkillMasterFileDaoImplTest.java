package com.srobinson24.sandbox.tradeskillmaster.dao.impl;

import com.srobinson24.sandbox.tradeskillmaster.dao.TradeSkillMasterItemDao;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Created by srobinso on 3/28/2017.
 */
@Ignore
public class TradeSkillMasterFileDaoImplTest {

    @Rule
    public final TemporaryFolder tempFile = new TemporaryFolder();

    @Test
    public void testSave1Entry() throws Exception {


        TradeSkillMasterItemDao tradeSkillMasterItemDao = new FileTradeSkillMasterItemDaoImpl(tempFile.newFile("testFile.txt"));

        TradeSkillMasterItem testTradeSkillMasterItem = new TradeSkillMasterItem();
        testTradeSkillMasterItem.setName("Enchant Neck - Mark of the Trained Soldier");
        testTradeSkillMasterItem.setId(141909);
        testTradeSkillMasterItem.setRawMinBuyout(109970000);

//        tradeSkillMasterItemDao.save(testTradeSkillMasterItem); //if not thrown, then we good

    }

    @Test
    public void testSave2Entries() throws Exception {
        TradeSkillMasterItemDao tradeSkillMasterItemDao = new FileTradeSkillMasterItemDaoImpl(tempFile.newFile("testFile.txt"));

        TradeSkillMasterItem testItem1 = new TradeSkillMasterItem();
        testItem1.setName("Enchant Neck - Mark of the Trained Soldier");
        testItem1.setId(141909);
        testItem1.setRawMinBuyout(109970000);

        TradeSkillMasterItem testItem2 = new TradeSkillMasterItem();
        testItem2.setName("foo");
        testItem2.setId(190000);
        testItem2.setRawMinBuyout(170000);

//        tradeSkillMasterItemDao.save(testItem1); //if not thrown, then we good
//        tradeSkillMasterItemDao.save(testItem2); //if not thrown, then we good

    }

    @Test
    public void testRead1Item() throws Exception {
        TradeSkillMasterItemDao tradeSkillMasterItemDao = new FileTradeSkillMasterItemDaoImpl(tempFile.newFile("testFile.txt"));

        TradeSkillMasterItem testTradeSkillMasterItem = new TradeSkillMasterItem();
        testTradeSkillMasterItem.setName("Enchant Neck - Mark of the Trained Soldier");
        testTradeSkillMasterItem.setId(141909);
        testTradeSkillMasterItem.setRawMinBuyout(109970000);

//        tradeSkillMasterItemDao.save(testTradeSkillMasterItem); //if not thrown, then we good

        final TradeSkillMasterItem readTradeSkillMasterItem = tradeSkillMasterItemDao.read(testTradeSkillMasterItem.getId());

        Assert.assertEquals(testTradeSkillMasterItem, readTradeSkillMasterItem);


    }

    @Test
    public void testSave2Read1Item() throws Exception {
        TradeSkillMasterItemDao tradeSkillMasterItemDao = new FileTradeSkillMasterItemDaoImpl(tempFile.newFile("testFile.txt"));

        TradeSkillMasterItem testItem1 = new TradeSkillMasterItem();
        testItem1.setName("Enchant Neck - Mark of the Trained Soldier");
        testItem1.setId(141909);
        testItem1.setRawMinBuyout(109970000);

        TradeSkillMasterItem testItem2 = new TradeSkillMasterItem();
        testItem2.setName("foo");
        testItem2.setId(190000);
        testItem2.setRawMinBuyout(170000);

//        tradeSkillMasterItemDao.save(testItem1); //if not thrown, then we good
//        tradeSkillMasterItemDao.save(testItem2); //if not thrown, then we good

        final TradeSkillMasterItem actualTsmItem = tradeSkillMasterItemDao.read(testItem1.getId());
        Assert.assertEquals(testItem1, actualTsmItem);


    }

}