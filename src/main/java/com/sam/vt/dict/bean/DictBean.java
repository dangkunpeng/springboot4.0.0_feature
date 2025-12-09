package com.sam.vt.dict.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictBean {
    private String dictCode;
    private String itemCode;
    private String itemName;

    public static DictBean getDictOfNameEqValue(String val) {
        return DictBean.builder().itemCode(val).itemName(val).build();
    }
}
