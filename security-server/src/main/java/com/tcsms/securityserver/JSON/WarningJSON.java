package com.tcsms.securityserver.JSON;

public class WarningJSON {
    private int code;
    private String message;//消息
    private Object data;//数据对象

    public WarningJSON(int code, String massege, Object result) {
        this.code = code;
        this.message = message;
        this.data = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMassege() {
        return message;
    }

    public void setMassege(String message) {
        this.message = message;
    }

    public Object getResult() {
        return data;
    }

    public void setResult(Object result) {
        this.data = result;
    }

    public String toString() {
        return "{" +
                "\"code\":" + code +
                ", \"message\":" + "\"" + message + "\"" +
                ", \"data\":" + data +
                "}";
    }

}
