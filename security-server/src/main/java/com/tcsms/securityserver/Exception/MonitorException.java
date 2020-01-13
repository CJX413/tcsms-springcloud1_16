package com.tcsms.securityserver.Exception;

public class MonitorException extends Exception {

    private Integer code;
    private String message;
    private Object data;

    public MonitorException(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code +
                ", \"massege\":" + "\"" + message + "\"" +
                ", \"data\":" + data +
                "}";
    }
}
