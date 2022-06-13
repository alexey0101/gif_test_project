package com.testwork.test.clients;

import com.testwork.test.models.ExchangeModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "exchange", url = "${exchange.url}")
public interface ExchangeService {
    @RequestMapping(value = "/historical/{dateString}.json?app_id=${exchange.apiKey}&base=${exchange.currency}", method = RequestMethod.GET)
    ExchangeModel getExchangeRates(@PathVariable String dateString, @RequestParam String symbols);

    @RequestMapping(value = "/latest.json?app_id=${exchange.apiKey}&base=${exchange.currency}", method = RequestMethod.GET)
    ExchangeModel getExchangeRates(@RequestParam String symbols);
}
