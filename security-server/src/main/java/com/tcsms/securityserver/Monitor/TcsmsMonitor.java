package com.tcsms.securityserver.Monitor;

public class TcsmsMonitor implements Runnable {

    private String threadName;

    public TcsmsMonitor(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void run() {

    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}
