package com.srobinson24.sandbox;


import com.srobinson24.sandbox.tradeskillmaster.service.PricingService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by srobinso on 3/24/2017.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        PricingService pricingService = (PricingService) context.getBean("secondTimeIsTheCharm");
        pricingService.getPricedEnchants();
        
    }
}
