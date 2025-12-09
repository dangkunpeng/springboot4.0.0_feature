package com.sam.vt.enums;

public enum EnumValid {
    YES(1, "YES"),
    NO(0, "NO");

    private String name;
    private Integer code;

    EnumValid(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }
}
