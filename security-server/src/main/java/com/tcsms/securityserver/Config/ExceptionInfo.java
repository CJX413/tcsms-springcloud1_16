package com.tcsms.securityserver.Config;

public enum ExceptionInfo {

    DEVICE_COLLISION_MONITOR_SEND_WARNING(600, "设备碰撞监听发送预警失败！"),
    DEVICE_COLLISION_MONITOR_STOP(601, "设备碰撞监听停止！"),
    ;
    private int code;
    private String msg;

    ExceptionInfo(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public int getCode() {
        return this.code;
    }
}
