/**
 * 文件名：HwException.java
 * 版权： Copyright 2017-2022 HMDM All Rights Reserved.
 * 描述：主数据管理系统
 */
package com.example.common.exception;

/**
 * @author or mingliang.chen@hand-china.com
 * @version 1.0
 * @date 2019/4/23 20:31
 */
public class HwException extends RuntimeException{
    private int code;

    public HwException() {}

    public HwException(int code) {
        this.code = code;
    }

    public HwException(String message) {
        super(message);
    }

    public HwException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
