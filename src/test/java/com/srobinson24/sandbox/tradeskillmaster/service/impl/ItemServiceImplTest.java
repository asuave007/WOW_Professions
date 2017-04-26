package com.srobinson24.sandbox.tradeskillmaster.service.impl;

import com.srobinson24.sandbox.tradeskillmaster.dao.TradeSkillMasterItemDao;
import com.srobinson24.sandbox.tradeskillmaster.domain.Enchant;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.service.ItemUpdateService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by srobinso on 3/24/2017.
 */
public class ItemServiceImplTest {
    @Test
    public void testFindItemsToUpdateItemNotOnDisk() throws Exception {
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
        final Set<Integer> actual = itemService.findItemsToUpdate(Collections.singleton(enchant));

        Assert.assertEquals("Expected size was one", 1, actual.size());

        final Integer actualIdReturned = actual.stream().findFirst().get();

        Assert.assertEquals(expected.getId(), (int) actualIdReturned);


    }

    @Test
    public void callUpdateService() throws Exception {
    }

}