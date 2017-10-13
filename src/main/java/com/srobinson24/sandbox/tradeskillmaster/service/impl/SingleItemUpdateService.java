package com.srobinson24.sandbox.tradeskillmaster.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.srobinson24.sandbox.tradeskillmaster.domain.TradeSkillMasterItem;
import com.srobinson24.sandbox.tradeskillmaster.exception.RuntimeHttpException;
import com.srobinson24.sandbox.tradeskillmaster.service.ItemUpdateService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * Created by srobinso on 4/18/2017.
 */
@Service
public class SingleItemUpdateService implements ItemUpdateService {

    private final Logger logger = LoggerFactory.getLogger(SingleItemUpdateService.class);

    @Value("${url}")
    private String url;

    @Override
    public TradeSkillMasterItem fetchUpdateItemInformation(int id) {
        final String urlWithId = StringUtils.replace(url, "{$id}", Integer.toString(id));
        final TradeSkillMasterItem tsmItem;
        try {
            tsmItem = callUpdateService(urlWithId);
        } catch (IOException ex) {
            throw new RuntimeHttpException(ex);
        }
        return tsmItem;
    }

    @Override
    public void updateItemsFromRemoteService(Set<TradeSkillMasterItem> itemsToUpdate) {
        itemsToUpdate.forEach(item -> {

            final TradeSkillMasterItem updatedTsmItem = fetchUpdateItemInformation(item.getId());
            BeanUtils.copyProperties(updatedTsmItem, item);
            item.setLastUpdate(LocalDateTime.now());
        });
    }

    @Override
    public Map<Integer, TradeSkillMasterItem> update() {
        return null;
    }

    private TradeSkillMasterItem callUpdateService(final String urlWithId) throws IOException {
        logger.info("About to call url: {}", urlWithId);
        final HttpResponse response = Request.Get(urlWithId).addHeader("Accept", "application/json").execute().returnResponse();
        logger.debug("Response code is: {}", response.getStatusLine().getStatusCode());

        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            final String responseString = entity != null ? EntityUtils.toString(entity) : null;
            return processHttpResponseString(responseString);
        }
        else {
            throw new RuntimeHttpException("Invalid HTTP response code: [" + status + "] with message: " + EntityUtils.toString(response.getEntity()));
        }
    }

    private TradeSkillMasterItem processHttpResponseString(final String responseContent) {
        logger.debug("Converting Http Response to Java Object: {}", responseContent);
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        try {
            return objectMapper.readValue(responseContent, TradeSkillMasterItem.class);
        } catch (IOException ex) {
            throw new RuntimeHttpException(ex);
        }

    }
}
