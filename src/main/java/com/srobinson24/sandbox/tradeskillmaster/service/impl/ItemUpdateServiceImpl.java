package com.srobinson24.sandbox.tradeskillmaster.service.impl;

import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.exception.RuntimeHttpException;
import com.srobinson24.sandbox.tradeskillmaster.service.ItemUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by srobinso on 5/9/2017.
 */
@Service
public class ItemUpdateServiceImpl implements ItemUpdateService {

    private final Logger logger = LoggerFactory.getLogger(ItemUpdateServiceImpl.class);

    private final RestOperations restTemplate;

    @Value("${all.items.url}")
    private String url;

    @Autowired
    public ItemUpdateServiceImpl(RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String addJsonApiKeyToUrl (final String url) {
        final String jsonApi = System.getProperty("apiKey");
        if (jsonApi == null) throw new IllegalArgumentException("Argument for json api key is required and was missing!");
        return url + jsonApi;
    }

    @Override
    public Map<Integer, TradeSkillMasterItem> fetchAllItemsFromExternalService() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity headerEntity = new HttpEntity<>(httpHeaders);

        final String urlWithApiKey = addJsonApiKeyToUrl(url);
        final ResponseEntity<TradeSkillMasterItem[]> responseEntity =
                restTemplate.exchange(urlWithApiKey, HttpMethod.GET, headerEntity, TradeSkillMasterItem[].class);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
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
