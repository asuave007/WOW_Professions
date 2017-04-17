package com.srobinson24.sandbox.tradeskillmaster.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by srobinso on 3/24/2017.
 */
public class TradeSkillMasterItemTest {

    @Test
    public void testItemSerialization() throws IOException {

        final File testFile = new File("src\\test\\resources\\testItem.json");
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

        final TradeSkillMasterItem actual = objectMapper.readValue(testFile, TradeSkillMasterItem.class);
        TradeSkillMasterItem expected = new TradeSkillMasterItem();
        expected.setName("Enchant Neck - Mark of the Trained Soldier");
        expected.setRawMinBuyout(124960000);
        expected.setId(141909);

        Assert.assertEquals(expected, actual);

    }

}