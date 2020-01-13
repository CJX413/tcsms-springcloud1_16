package com.tcsms.securityserver.Monitor;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class MonitorManager {
    private static Map<String, Thread> map = new HashMap<>();

    public void addMonitor(Thread thread) {
        map.put(thread.getName(), thread);
    }

    public void shutDownAllMonitor() {
        map.forEach((key, thread) -> {
            log.info(key+"--"+thread.getName());
            thread.interrupt();
        });
    }

    public void openMonitor() {

    }
}
