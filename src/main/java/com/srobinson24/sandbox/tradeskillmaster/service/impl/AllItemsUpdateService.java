package com.srobinson24.sandbox.tradeskillmaster.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.exception.RuntimeHttpException;
import com.srobinson24.sandbox.tradeskillmaster.service.ItemUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by srobinso on 5/9/2017.
 */
@Service
public class AllItemsUpdateService implements ItemUpdateService {

    private final Logger logger = LoggerFactory.getLogger(AllItemsUpdateService.class);

    @Value("${all.items.url}")
    private String url;

    final RestOperations restTemplate;

    @Autowired
    public AllItemsUpdateService(RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }


//    private TradeSkillMasterItem updateItemsFromRemoteService(final String urlWithId) throws IOException {
//        logger.info("About to call url: {}", urlWithId);
//        final HttpResponse response = Request.Get(urlWithId).addHeader("Accept", "application/json").execute().returnResponse();
//        logger.debug("Response code is: {}", response.getStatusLine().getStatusCode());
//
//        int status = response.getStatusLine().getStatusCode();
//        if (status >= 200 && status < 300) {
//            HttpEntity entity = response.getEntity();
//            final String responseString = entity != null ? EntityUtils.toString(entity) : null;
//            return processHttpResponseString(responseString);
//        }
//        else {
//            throw new RuntimeHttpException("Invalid HTTP response code: [" + status + "] with message: " + EntityUtils.toString(response.getEntity()));
//        }
//    }

    private TradeSkillMasterItem callUpdateService() {
        return null;
    }

    @Override
    public TradeSkillMasterItem fetchUpdateItemInformation(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateItemsFromRemoteService(Set<TradeSkillMasterItem> itemsToUpdate) throws URISyntaxException {
        logger.info("starting using: {}", itemsToUpdate);

//        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//        MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
//        final ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
//        jsonMessageConverter.setObjectMapper(objectMapper);
//        messageConverters.add(jsonMessageConverter);
//        new RestTemplate(messageConverters);


        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity headerEntity = new HttpEntity<>(httpHeaders);




//        final RequestEntity<Set<TradeSkillMasterItem>> build = RequestEntity.get(new URI(allItemsUrl)).accept(MediaType.APPLICATION_JSON).build();

        final ResponseEntity<TradeSkillMasterItem[]>
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, headerEntity, TradeSkillMasterItem[].class);

        if(responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeHttpException("Invalid HTTP response code: " + responseEntity.getStatusCode());
        }

        final TradeSkillMasterItem[] body = responseEntity.getBody();
        logger.info("body processed");

        final List<TradeSkillMasterItem> tradeSkillMasterItems = Arrays.asList(responseEntity.getBody());
        logger.info("list loaded, size: {}", tradeSkillMasterItems.size());
//        tradeSkillMasterItems.addAll(tradeSkillMasterItems);

        final HashSet<TradeSkillMasterItem> itemsFromRemoteSource = Sets.newHashSet(responseEntity.getBody());

//        final Map<Integer, TradeSkillMasterItem> allItems = itemsFromRemoteSource.parallelStream().collect(Collectors.toMap(TradeSkillMasterItem::getId, item -> item));
        final Map<Integer, TradeSkillMasterItem> allItems =
                itemsFromRemoteSource.parallelStream().collect(Collectors.toMap(TradeSkillMasterItem::getId, item -> item));
//                .collect(Collectors.groupingBy(TradeSkillMasterItem::getId), Collectors.flat()));

        logger.info("all loaded, total size is: {}", itemsFromRemoteSource.size());

        itemsToUpdate.forEach( i -> {
            final TradeSkillMasterItem rawItem = allItems.get(i.getId());
            Preconditions.checkNotNull(rawItem,"Item was null: " + i.getId());
            BeanUtils.copyProperties(rawItem,i);
            i.setLastUpdate(LocalDateTime.now());

        });


        logger.info("ending with: {}", itemsToUpdate);



//        restTemplate.
//
//
//        itemsToUpdate.forEach(item -> {
//
//            final TradeSkillMasterItem updatedTsmItem = fetchUpdateItemInformation(item.getId());
//            BeanUtils.copyProperties(updatedTsmItem, item);
//            item.setLastUpdate(LocalDateTime.now());
//        });
    }

    @Override
    public Map<Integer, TradeSkillMasterItem> update() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity headerEntity = new HttpEntity<>(httpHeaders);

        final ResponseEntity<TradeSkillMasterItem[]>
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, headerEntity, TradeSkillMasterItem[].class);

        if(responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeHttpException("Invalid HTTP response code: " + responseEntity.getStatusCode());
        }

        final TradeSkillMasterItem[] body = responseEntity.getBody();
        logger.info("body processed");

        final List<TradeSkillMasterItem> tradeSkillMasterItems = Arrays.asList(body);
        logger.info("list loaded, size: {}", tradeSkillMasterItems.size());

        final LocalDateTime currentTime = LocalDateTime.now();
        tradeSkillMasterItems.forEach(item -> item.setLastUpdate(currentTime));
        logger.info("Finished setting Timestamp to: {}", currentTime);

        return tradeSkillMasterItems.parallelStream().collect(Collectors.toMap(TradeSkillMasterItem::getId, e -> e));
    }
}
