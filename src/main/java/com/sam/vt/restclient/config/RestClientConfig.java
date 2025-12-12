package com.sam.vt.restclient.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://api.example.com")
                .defaultHeader("X-Client-ID", "my-client")
                .requestInterceptor((request, body, execution) -> {
                    // 请求拦截器
                    long start = System.currentTimeMillis();
                    ClientHttpResponse response = execution.execute(request, body);
                    long duration = System.currentTimeMillis() - start;
                    log.info("请求耗时: {}ms, URI: {}", duration, request.getURI().getPath());
                    return response;
                })
                .build();
    }

    // 使用 Apache HttpClient 5
    @Bean
    public RestClient restClientWithApache() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(10))
                .setConnectionRequestTimeout(Timeout.ofSeconds(10))
                .setResponseTimeout(Timeout.ofSeconds(30))
                .build();
        CloseableHttpClient httpClients = HttpClients.custom()
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .setDefaultRequestConfig(requestConfig)
                .build();
        return RestClient.builder()
                .baseUrl("https://api.example.com")
                .requestFactory(new HttpComponentsClientHttpRequestFactory(httpClients))
                .build();
    }
}