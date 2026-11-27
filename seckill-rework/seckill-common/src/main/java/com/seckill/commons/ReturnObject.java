package com.seckill.commons;

/**
 * 许彤
 * 2020/12/26
 */
public class ReturnObject<T> {
    private int code;
    private String message;
    private T result;

    public int getCode() {
        return code;
    }

    public ReturnObject<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ReturnObject<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getResult() {
        return result;
    }

    public ReturnObject<T> setResult(T result) {
        this.result = result;
        return this;
    }
}
