package com.tcsms.securityserver.Monitor;

import com.google.gson.JsonArray;
import com.tcsms.securityserver.Config.ExceptionInfo;
import com.tcsms.securityserver.Config.WarningInfo;
import com.tcsms.securityserver.Exception.SendWarningFailedException;
import com.tcsms.securityserver.Service.ServiceImp.RedisServiceImp;
import com.tcsms.securityserver.Service.ServiceImp.RestTemplateServiceImp;

import java.util.List;

public abstract class TcsmsMonitor extends Thread {

    private String threadName;
    RestTemplateServiceImp restTemplateService;
    RedisServiceImp redisServiceImp;
    private boolean pause = false;

    TcsmsMonitor(String threadName) {
        super(threadName);
        this.threadName = threadName;
    }

    public abstract JsonArray getData();

    public abstract List<WarningInfo> isWarning();

    void pause() {
        this.pause = true;
    }

    void awake() {
        synchronized (this) {
            this.pause = false;
            this.notify();
        }
    }

    void sendWarning(WarningInfo warningInfo, JsonArray data) throws SendWarningFailedException {
        restTemplateService.sendWarning(warningInfo, data);
    }

    void sendException(ExceptionInfo exceptionInfo, JsonArray data) {
        restTemplateService.sendException(exceptionInfo, data);
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public void wait(Object object) throws InterruptedException {
        if (pause) {
            object.wait();
        }
    }

    public boolean isPause() {
        return this.pause;
    }
}
