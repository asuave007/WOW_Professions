package com.srobinson24.sandbox;


import com.srobinson24.sandbox.tradeskillmaster.service.PricingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by srobinso on 3/24/2017.
 */
public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {

        if (args == null || args[0] == null) throw new IllegalArgumentException("Argument for json api key is required and was missing!");

        final String jsonApiKey = args[0];
        logger.debug("Json API key from command line is: {}", jsonApiKey);
        System.setProperty("apiKey", jsonApiKey);

        ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        PricingService pricingService = (PricingService) context.getBean("pricingServiceImpl");
        pricingService.getPricedEnchants();
        
    }
}
