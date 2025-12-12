package com.sam.vt.restclient;

import com.sam.vt.dict.bean.DictBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.rmi.ServerException;
import java.util.concurrent.CompletableFuture;

@Service
public class RestClientService {
    
    // 1. 创建 RestClient
    private final RestClient restClient = RestClient.builder()
        .baseUrl("https://api.example.com")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.USER_AGENT, "Spring-RestClient")
        .build();
    
    // 2. GET 请求 (阻塞式)
    public DictBean getUserById(Long id) {
        return restClient.get()
            .uri("/users/{id}", id)
            .header("Authorization", "Bearer token")
            .retrieve()  // 发送请求
            .body(DictBean.class);  // 直接返回对象
    }
    
    // 3. GET 请求带响应状态
    public ResponseEntity<DictBean> getUserByIdWithStatus(Long id) {
        return restClient.get()
            .uri("/users/{id}", id)
            .retrieve()
            .toEntity(DictBean.class);  // 返回 ResponseEntity
    }
    
    // 4. POST 请求
    public DictBean createUser(DictBean dictBean) {
        return restClient.post()
            .uri("/users")
            .body(dictBean)  // 设置请求体
            .retrieve()
            .body(DictBean.class);
    }
    
    // 5. 错误处理
    public DictBean getUserWithErrorHandling(Long id) throws Exception {
        try {
            return restClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    try {
                        throw new Exception("用户不存在");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .onStatus(status -> status.is5xxServerError(), (request, response) -> {
                    throw new ServerException("服务器错误");
                })
                .body(DictBean.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new Exception("用户不存在", e);
        }
    }
    
    // 6. 异步调用 (可选)
    public CompletableFuture<DictBean> getUserAsync(Long id) {
        return CompletableFuture.supplyAsync(() ->
            restClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .body(DictBean.class)
        );
    }
    
    // 7. 文件上传
    public String uploadFile(MultipartFile file) {
        return restClient.post()
            .uri("/upload")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(new MultipartBodyBuilder()
                .part("file", file.getResource()))
            .retrieve()
            .body(String.class);
    }
}