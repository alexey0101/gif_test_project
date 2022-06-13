package com.testwork.test.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "giphy", url = "${giphy.url}")
public interface GifService {
    @RequestMapping(value = "/random?api_key=${giphy.apiKey}", method = RequestMethod.GET)
    String getGif(@RequestParam("tag") String tag);
}
