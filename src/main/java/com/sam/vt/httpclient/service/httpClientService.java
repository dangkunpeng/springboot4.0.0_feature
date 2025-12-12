package com.sam.vt.httpclient.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.StatusLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class httpClientService {

    @Autowired
    private CloseableHttpClient closeableHttpClient;

    public String getData(String url) {
        HttpGet httpGet = new HttpGet(url);
        // Add headers
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("User-Agent", "SpringBoot-App");

        try (ClassicHttpResponse response = closeableHttpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            // Ensure the entity content is fully consumed
            EntityUtils.consume(entity);

            int statusCode = response.getCode();
            if (statusCode >= 200 && statusCode < 300) {
                return responseBody;
            }
            log.error("Request failed with status: {}", new StatusLine(response));
            throw new RuntimeException("API request failed: " + response.getCode());
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}