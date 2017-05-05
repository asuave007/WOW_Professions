package com.srobinson24.sandbox.tradeskillmaster.service.impl;

import com.google.common.collect.Sets;
import com.srobinson24.sandbox.tradeskillmaster.dao.TradeSkillMasterItemDao;
import com.srobinson24.sandbox.tradeskillmaster.domain.Enchant;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.service.ItemUpdateService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by srobinso on 3/24/2017.
 */
public class ItemServiceImplTest {
    @Test
    public void testUpdateFromCacheItemNotOnDisk() throws Exception {

        final LocalDateTime lastUpdate = LocalDateTime.now().minusMinutes(30);
        final TradeSkillMasterItem expected = new TradeSkillMasterItem();
        expected.setId(38);

        final TradeSkillMasterItemDao mockDao = Mockito.mock(TradeSkillMasterItemDao.class);
        Mockito.when(mockDao.read(38)).thenReturn(null);

        final ItemUpdateService mockUpdateService = Mockito.mock(ItemUpdateService.class);
        Mockito.when(mockUpdateService.fetchUpdateItemInformation(38)).thenReturn(expected);


        final ItemServiceImpl itemService = new ItemServiceImpl(mockDao, mockUpdateService, null, null);
        Enchant enchant = new Enchant();
        enchant.setId(38);
        enchant.setName("Mark of the Trained Soldier");
        itemService.updateFromCache(Collections.singleton(enchant));

        Assert.assertEquals(null, enchant.getLastUpdate());

    }

    @Test
    public void testUpdateFromCacheItemIsOnDisk() throws Exception {

        final TradeSkillMasterItem expected = new TradeSkillMasterItem();
        expected.setId(38);
        final LocalDateTime lastUpdate = LocalDateTime.now().minusMinutes(30);
        expected.setLastUpdate(lastUpdate); //updated 30 min ago

        final TradeSkillMasterItemDao mockDao = Mockito.mock(TradeSkillMasterItemDao.class);
        Mockito.when(mockDao.read(38)).thenReturn(expected);

        final ItemUpdateService mockUpdateService = Mockito.mock(ItemUpdateService.class);
        Mockito.when(mockUpdateService.fetchUpdateItemInformation(38)).thenReturn(expected);


        final ItemServiceImpl itemService = new ItemServiceImpl(mockDao, mockUpdateService, null, null);
        ReflectionTestUtils.setField(itemService,"minutesBeforeUpdate", 60); //set update refresh interval to 60 min

        Enchant enchant = new Enchant();
        enchant.setId(38);
        enchant.setName("Mark of the Trained Soldier");
        itemService.updateFromCache(Collections.singleton(enchant));

        Assert.assertEquals(lastUpdate, enchant.getLastUpdate());

    }

    @Test
    public void testFindItemsToUpdate() throws Exception {
        final ItemServiceImpl itemService = new ItemServiceImpl(null, null, null, null);
        ReflectionTestUtils.setField(itemService,"minutesBeforeUpdate", 60); //set update refresh interval to 60 min

        Enchant enchant1 = new Enchant();
        enchant1.setId(1);
        enchant1.setLastUpdate(LocalDateTime.now());


        Enchant enchant2 = new Enchant();
        enchant2.setId(2);
        enchant2.setLastUpdate(null);

        final Set<TradeSkillMasterItem> itemsToUpdate = itemService.findItemsToUpdate(Sets.newHashSet(enchant1, enchant2));

        Assert.assertEquals(1, itemsToUpdate.size());

        Assert.assertEquals(itemsToUpdate.stream().findFirst().get(),enchant2);
    }

}