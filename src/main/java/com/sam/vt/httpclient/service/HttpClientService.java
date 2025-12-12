package com.sam.vt.httpclient.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.StatusLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class HttpClientService {
    public static final String CONTENT_TYPE = "application/json";
    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CloseableHttpClient closeableHttpClient;

    public String getData(String url) {
        HttpGet httpGet = new HttpGet(url);
        // Add headers
        httpGet.setHeader("Accept", CONTENT_TYPE);
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
    /**
     * POST request with JSON body
     *
     * @param url
     * @param requestBody
     * @return
     */
    public String postJson(String url, Object requestBody) {
        HttpPost httpPost = new HttpPost(url);

        // Convert request body to JSON
        String json = objectMapper.writeValueAsString(requestBody);
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);

        // Add headers
        httpPost.setHeader("Accept", CONTENT_TYPE);
        httpPost.setHeader("Content-Type", CONTENT_TYPE);

        try (ClassicHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity responseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);

            if (response.getCode() >= 200 && response.getCode() < 300) {
                return responseBody;
            }
            throw new RuntimeException("POST request failed: " + response.getCode());
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * POST request with JSON body
     *
     * @param url
     * @param requestBody
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> T postJson(String url, Object requestBody, Class<T> responseType) {
        HttpPost httpPost = new HttpPost(url);

        // Convert request body to JSON
        String json = objectMapper.writeValueAsString(requestBody);
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);

        // Add headers
        httpPost.setHeader("Accept", CONTENT_TYPE);
        httpPost.setHeader("Content-Type", CONTENT_TYPE);

        try (ClassicHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity responseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);

            if (response.getCode() >= 200 && response.getCode() < 300) {
                return objectMapper.readValue(responseBody, responseType);
            }
            throw new RuntimeException("POST request failed: " + response.getCode());
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}