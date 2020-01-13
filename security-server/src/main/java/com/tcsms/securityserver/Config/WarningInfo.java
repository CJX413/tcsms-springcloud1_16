package com.tcsms.securityserver.Config;

public enum WarningInfo {
    DEVICE_COLLISION_YELLOW_WARNING(500,"设备间碰撞黄色警报！"),
    DEVICE_COLLISION_RED_WARNING(501,"设备间碰撞红色警报！"),
    ;
    private int code;
    private String msg;

    WarningInfo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
