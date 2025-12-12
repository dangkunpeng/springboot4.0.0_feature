package com.sam.vt.httpclient;

import com.sam.vt.httpclient.service.HttpClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/httpClient")
public class HttpClientController {

    private final HttpClientService httpClientService;

    @RequestMapping("/getData")
    public String getData() {
        String url = "http://localhost:8080/api/env/all";
        return httpClientService.getData(url);
    }

    @RequestMapping("/getData/{version}")
    public String getDataVersion(@PathVariable String version) {
        String url = "http://localhost:8080/api/user?version=" + version;
        return httpClientService.getData(url);
    }
}
