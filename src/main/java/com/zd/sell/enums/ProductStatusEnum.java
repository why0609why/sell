package com.zd.sell.enums;

import lombok.Getter;

/**
 * 产品上架状态的枚举类型
 */
@Getter
public enum  ProductStatusEnum {
    UP(0, "上架中"),
    Down(1, "下架");

    private Integer code;
    private String message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
