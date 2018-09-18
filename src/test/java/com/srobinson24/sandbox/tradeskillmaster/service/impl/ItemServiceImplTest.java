package com.srobinson24.sandbox.tradeskillmaster.service.impl;

import com.google.common.collect.Sets;
import com.srobinson24.sandbox.tradeskillmaster.dao.TradeSkillMasterItemDao;
import com.srobinson24.sandbox.tradeskillmaster.domain.CraftableItem;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.startsWith;

/**
 * Created by srobinso on 3/24/2017.
 */
public class ItemServiceImplTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void testUpdateFromCacheItemNotOnDisk() throws Exception {

        final TradeSkillMasterItem expected = new TradeSkillMasterItem();
        expected.setId(38);

        final TradeSkillMasterItemDao mockDao = Mockito.mock(TradeSkillMasterItemDao.class);
        Mockito.when(mockDao.readAll()).thenReturn(Collections.EMPTY_MAP);

        final ItemServiceImpl itemService = new ItemServiceImpl(mockDao, null, null, null);
        CraftableItem craftableItem = new CraftableItem();
        craftableItem.setId(38);
        craftableItem.setName("Mark of the Trained Soldier");

        thrown.expect(RuntimeException.class);
        thrown.expectMessage(startsWith("Item ID was not found in list of all items"));
        itemService.updateItemsToPrice(Collections.singleton(craftableItem));


    }

    @Test
    public void testFindItemsToUpdate() throws Exception {
        final ItemServiceImpl itemService = new ItemServiceImpl(null, null, null, null);
        ReflectionTestUtils.setField(itemService,"minutesBeforeUpdate", 60); //set update refresh interval to 60 min

        CraftableItem craftableItem1 = new CraftableItem();
        craftableItem1.setName("first");
        craftableItem1.setId(1);
        craftableItem1.setLastUpdate(LocalDateTime.now().minusMinutes(10));


        CraftableItem craftableItem2 = new CraftableItem();
        craftableItem2.setName("second");
        craftableItem2.setId(2);
        craftableItem2.setLastUpdate(null);

        CraftableItem craftableItem3 = new CraftableItem();
        craftableItem3.setName("third");
        craftableItem3.setId(3);
        craftableItem3.setLastUpdate(LocalDateTime.now().minusMinutes(90));

        final Set<TradeSkillMasterItem> itemsToUpdate = itemService.findItemsToUpdate(Sets.newHashSet(craftableItem1, craftableItem2, craftableItem3));

        Assert.assertEquals(2, itemsToUpdate.size());

        Assert.assertEquals(itemsToUpdate,Sets.newHashSet(craftableItem2, craftableItem3));
    }

}