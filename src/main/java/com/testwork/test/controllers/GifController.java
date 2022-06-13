package com.testwork.test.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testwork.test.clients.ExchangeService;
import com.testwork.test.clients.GifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@RestController
public class GifController {

    @Autowired
    private GifService gifService;

    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/")
    public String getGif(@RequestParam("currency") String currency) {
        try {
            String gifInfo = checkCurrencyStrengthening(currency) ?
                    gifService.getGif("rich") : gifService.getGif("broke");

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode gifJson = objectMapper.readTree(gifInfo);
            String gifSource = gifJson.get("data").get("images").get("downsized_large").get("url").toString();

            return String.format("<img style=\"display: block;\n" +
                    "  margin-left: auto;\n" +
                    "  margin-right: auto;\n" +
                    "  width: 50%% ;\" src=%s>", gifSource);

        } catch (Exception e) {
            return String.format("<b><p style=\"text-align: center" +
                    "\">%s<p><b>", e.getMessage());
        }
    }

    /**
     * Checks if the exchange rate has strengthened against
     * the currency declared in the application.properties during the previous day.
     *
     * @param currency
     * @return
     */
    private boolean checkCurrencyStrengthening(String currency) {
        BigDecimal yesterdayRate = getYesterdayRate(currency);
        BigDecimal todayRate = getTodayRate(currency);

        if (yesterdayRate == null || todayRate == null) {
            throw new IllegalArgumentException("Rate for the given currency wasn't found!");
        }

        int ratesCompare = yesterdayRate.compareTo(todayRate);
        return ratesCompare == 0 || ratesCompare == 1;
    }

    /**
     * Gets the yesterday's exchange rate of the given currency for the currency specified in application.properties
     *
     * @param currency (3-letter code)
     * @return
     */
    public BigDecimal getYesterdayRate(String currency) {
        return exchangeService.
                getExchangeRates(Instant.now().minus(1, ChronoUnit.DAYS)
                        .atZone(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), currency)
                .getRates().get(currency);
    }

    /**
     * Gets the current exchange rate of the given currency for the currency specified in application.properties
     *
     * @param currency (3-letter code)
     * @return
     */
    public BigDecimal getTodayRate(String currency) {
        return exchangeService.
                getExchangeRates(currency)
                .getRates().get(currency);
    }
}
