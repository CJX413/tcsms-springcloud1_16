package com.tcsms.securityserver.Config;

public class ConstantConfig {
    public static int SAFE_DISTANCE = 2;//塔吊的安全距离
    public static double SAFE_TORQUE = 0.95;//塔吊的安全力矩百分比
    public static double MODERATE_BREEZE = 5.5;//四级风速
    public static double STRONG_BREEZE = 10.8;//六级风速
    public static double G = 10;
    public static long ALLOWED_NOT_RUNNING_TIME = 10000;//设备多久不运行就休眠监控器---1800000(半个小时)
    public static final String REGISTRY_KEY = "_registry";//每台塔吊的注册情况在redis缓存中的key值的后缀
    public static final String REGISTERED = "1";
    public static final String UN_REGISTERED = "0";
    public static final String WARNING_RECEIVE_URL = "http://localhost:8080/warning";
    public static final String ERROE_RECEIVE_URL = "http://localhost:8080/error";
    public static final String MANAGER_MONITOR_THREAD = "ManagerMonitor";

}