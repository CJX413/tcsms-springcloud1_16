package com.tcsms.securityserver.JSON;

public class SendJSON {
    private Integer code;
    private String message;
    private Object data;

    public SendJSON(Integer code, String message, Object data) {
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
