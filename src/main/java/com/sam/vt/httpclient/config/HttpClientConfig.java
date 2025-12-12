package com.sam.vt.httpclient.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        return PoolingHttpClientConnectionManagerBuilder.create()
                // Maximum total connections
                .setMaxConnTotal(200)
                // Maximum connections per route
                .setMaxConnPerRoute(50)
                .setDefaultSocketConfig(SocketConfig.custom()
                        .setSoTimeout(Timeout.ofSeconds(30))
                        .build())
                .build();
    }

    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                // Connection timeout
                .setConnectTimeout(Timeout.ofSeconds(10))
                // Request timeout
                .setConnectionRequestTimeout(Timeout.ofSeconds(10))
                // Response timeout
                .setResponseTimeout(Timeout.ofSeconds(30))
                .build();
    }

    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager connManager, RequestConfig reqConfig) {

        return HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(reqConfig)
                // Retry strategy
                .setRetryStrategy(new DefaultHttpRequestRetryStrategy(3, Timeout.ofSeconds(2)))
                // Clean expired connections
                .evictExpiredConnections()
                // Clean idle connections
                .evictIdleConnections(Timeout.ofMinutes(5))
                .build();
    }
}