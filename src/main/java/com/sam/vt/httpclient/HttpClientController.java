package com.sam.vt.httpclient;

import com.sam.vt.dict.DictApi;
import com.sam.vt.dict.bean.DictBean;
import com.sam.vt.httpclient.service.HttpClientService;
import com.sam.vt.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/httpClient")
public class HttpClientController {

    private final DictApi dictApi;
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

    @PostMapping("/dict")
    public String dict(@RequestBody DictBean dictBean) {
        log.info("outer dictBean={}", dictBean);
        String url = "http://localhost:8080/api/httpClient/dictApi";
        Map dictMap =  httpClientService.postJson(url, dictBean, Map.class);
        return JsonUtil.toJsonString(dictMap);
    }

    @PostMapping("/dictApi")
    public String getDict(@RequestBody DictBean dictBean) {
        log.info("inner dictBean={}", dictBean);
        Map<String, String> dictMap = dictApi.getDictMap(dictBean.getDictCode());
        return JsonUtil.toJsonString(dictMap);
    }
}
