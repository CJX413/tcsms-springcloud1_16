package com.tcsms.securityserver.Config;

public class ConstantConfig {
    public static int SAFE_DISTANCE = 2;//塔吊的安全距离
    public static double G = 10;
    public static final String REGISTRY_KEY = "_registry";//每台塔吊的注册情况在redis缓存中的key值的后缀
    public static final String REGISTERED = "1";
    public static final String UN_REGISTERED = "0";
    public static final String WARNING_RECEIVE_URL = "http://localhost:8080/warning";
    public static final String ERROE_RECEIVE_URL = "http://localhost:8080/error";
    public static final String SECURITY_THREAD_PREFIX = "SecurityThread-";

    /**
     * 警报码
     */

    public static final int DEVICE_COLLISION_YELLOW_WARNING_CODE = 500;//设备间碰撞黄色警报码
    public static final int DEVICE_COLLISION_RED_WARNING_CODE = 501;//设备间碰撞红色警报码


    /**
     * 异常码
     */


}