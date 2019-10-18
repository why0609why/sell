package com.zd.sell.VO;


import lombok.Data;

/**
 * http请求返回的最外层对象
 */
@Data
public class ResultVO<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态信息
     */
    private String msg;

    /**
     * 返回的具体内容
     */
    private T data;

    public ResultVO() {
    }
}
