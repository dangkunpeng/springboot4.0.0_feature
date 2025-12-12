package com.sam.vt.restclient;

import com.sam.vt.dict.bean.DictBean;
import com.sam.vt.restclient.service.RestClientService;
import com.sam.vt.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restClient")
public class RestClientController {
    private final RestClientService restClientService;

    @RequestMapping("/env")
    public String getEnvData() {
        return restClientService.getEnv();
    }

    @RequestMapping("/dictMap")
    public String getDictMap(@RequestBody DictBean dictBean) {
        Map map = restClientService.getDictMap(dictBean);
        return JsonUtil.toJsonString(map);
    }
}
