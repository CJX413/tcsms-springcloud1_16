package com.tcsms.securityserver.Monitor;

import com.google.gson.JsonArray;
import com.tcsms.securityserver.Config.WarningInfo;
import com.tcsms.securityserver.Exception.SendWarningFailedException;
import com.tcsms.securityserver.Service.ServiceImp.RedisServiceImp;
import com.tcsms.securityserver.Service.ServiceImp.RestTemplateServiceImp;

import java.util.List;

import static com.tcsms.securityserver.Config.ConstantConfig.ERROE_RECEIVE_URL;

public abstract class TcsmsMonitor implements Runnable {

    private String threadName;
    RestTemplateServiceImp restTemplateService;
    RedisServiceImp redisServiceImp;

    TcsmsMonitor(String threadName) {
        this.threadName = threadName;
    }

    public abstract JsonArray getData();

    public abstract List<WarningInfo> isWarning();


    void sendWarning(WarningInfo warningInfo, JsonArray data) throws SendWarningFailedException {
        restTemplateService.sendWarning(warningInfo, data);
    }

    void sendException(String exception) {
        restTemplateService.sendJson(ERROE_RECEIVE_URL, exception);
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}
