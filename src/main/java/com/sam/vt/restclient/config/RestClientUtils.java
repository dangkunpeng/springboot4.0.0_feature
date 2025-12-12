package com.sam.vt.restclient.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

public class RestClientUtils {
    private static RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(Timeout.ofSeconds(10))
            .setConnectionRequestTimeout(Timeout.ofSeconds(10))
            .setResponseTimeout(Timeout.ofSeconds(30))
            .build();
    private static CloseableHttpClient httpClients = HttpClients.custom()
            .setConnectionManager(new PoolingHttpClientConnectionManager())
            // Retry strategy
            .setRetryStrategy(new DefaultHttpRequestRetryStrategy(3, Timeout.ofSeconds(2)))
            .setDefaultRequestConfig(requestConfig)
            // Clean expired connections
            .evictExpiredConnections()
            // Clean idle connections
            .evictIdleConnections(Timeout.ofMinutes(5))
            .build();

    public static RestClient getRestClient(String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "Spring-RestClient")
                .requestFactory(new HttpComponentsClientHttpRequestFactory(httpClients))
                .build();
    }
}