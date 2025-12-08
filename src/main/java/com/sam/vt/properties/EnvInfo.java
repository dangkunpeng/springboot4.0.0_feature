package com.sam.vt.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvInfo {
    private String name;
    private String server;
    private String url;
    private String userNm;
    private String passWd;

}
