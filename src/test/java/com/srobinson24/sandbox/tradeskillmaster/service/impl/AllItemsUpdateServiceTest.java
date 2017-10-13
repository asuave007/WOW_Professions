package com.srobinson24.sandbox.tradeskillmaster.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.srobinson24.sandbox.tradeskillmaster.domain.Enchant;
import org.junit.Test;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by srobinso on 5/9/2017.
 */
public class AllItemsUpdateServiceTest {
    @Test
    public void updateItemsFromRemoteService() throws Exception {
        final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        final MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        jsonMessageConverter.setObjectMapper(objectMapper);
        messageConverters.add(jsonMessageConverter);
        final RestTemplate restTemplate = new RestTemplate(messageConverters);

        AllItemsUpdateService updateService = new AllItemsUpdateService(restTemplate);

        ReflectionTestUtils.setField(updateService, "url", "http://api.tradeskillmaster.com/v1/item/US/uldum?format=json&apiKey=tqCLasiWDgcsd5adyBT7viRKPHS5aGsx");


        Enchant enchant = new Enchant();
        enchant.setId(38);
        enchant.setName("Mark of the Trained Soldier");

        updateService.updateItemsFromRemoteService(Collections.singleton(enchant));
    }

}