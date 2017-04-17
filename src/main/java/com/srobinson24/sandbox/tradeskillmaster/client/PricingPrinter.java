package com.srobinson24.sandbox.tradeskillmaster.client;

import com.srobinson24.sandbox.tradeskillmaster.service.PricingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by srobinso on 4/12/2017.
 */
@Component
public class PricingPrinter {

    private final PricingService pricingService;

public PricingPrinter(@Qualifier ("secondTimeIsTheCharm") PricingService pricingService) {
        this.pricingService = pricingService;
    }


    public void printPricesToCommandLine() throws IOException {
        pricingService.getPricedEnchants();
    }

}
