package com.tcsms.securityserver.Monitor;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class MonitorManager {
    private static Map<String, Thread> map = new HashMap<>();
    public static boolean turn_on = false;
    private static Map<String, Integer> warningCount = new HashMap<>();

    public static void addMonitor(Thread thread) {
        map.put(thread.getName(), thread);
    }

    public static void shutDownAllMonitor() {
        map.forEach((key, thread) -> {
            log.info(key + "--" + thread.getName());
            thread.interrupt();
        });
    }

    public static Integer getWarningCount(String device) {
        return warningCount.getOrDefault(device, null);
    }

    public static void addWarningCount(String deviceId) {
        Integer count = warningCount.getOrDefault(deviceId, null);
        if (count != null) {
            count = count + 1;
            warningCount.put(deviceId, count);
        }
    }

    public static boolean pauseMonitorByName(String deviceName) {
        Thread thread = map.getOrDefault(deviceName, null);
        if (thread != null) {
            try {
                thread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                dashdoiah
            }
            return true;
        }
        return false;
    }


    public void openMonitor() {

    }
}
