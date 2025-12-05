package com.sam.demo.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvInfo {
    private String serverName;
    private String envServer;
    private String envUrl;
    private String envUserNm;
    private String envPassWd;

}
