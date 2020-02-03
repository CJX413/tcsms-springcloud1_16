package com.tcsms.securityserver.Monitor;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Log4j2
public class MonitorManager {
    private static Map<String, TcsmsMonitor> map = new HashMap<>();
    public static boolean turn_on = false;
    private static Map<String, Integer> warningCount = new HashMap<>();

    public static void addMonitor(TcsmsMonitor monitor) {
        map.put(monitor.getName(), monitor);
    }

    public static void shutDownAllMonitor() {
        map.forEach((key, monitor) -> {
            log.info(key + "--" + monitor.getName());
            monitor.interrupt();
        });
    }

    public static Integer getWarningCount(String device) {
        Integer count = warningCount.getOrDefault(device, null);
        if (count != null) {
            return count;
        }
        return 0;
    }

    public static void addWarningCount(String deviceId) {
        Integer count = warningCount.getOrDefault(deviceId, null);
        if (count != null) {
            count = count + 1;
            warningCount.put(deviceId, count);
        }
    }

    public static void pauseMonitorByName(String deviceName) {
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            String pattern = ".*?" + deviceName + ".*?";
            boolean isMatch = Pattern.matches(pattern, key);
            if (isMatch) {
                TcsmsMonitor monitor = map.getOrDefault(key, null);
                if (monitor != null && !monitor.isPause()) {
                    monitor.pause();
                    log.info(deviceName + "-------这个设备的监听器已经暂停！");
                }
            }
        }
    }

    public static void notifyAllMonitor() {
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            TcsmsMonitor monitor = map.getOrDefault(key, null);
            if (monitor != null && monitor.isPause()) {
                monitor.awake();
                log.info(key + "-------这个设备的监听器已经唤醒！");
            }
        }
    }

    public static void notifyMonitorByName(String deviceName) {
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            String pattern = ".*?" + deviceName + ".*?";
            boolean isMatch = Pattern.matches(pattern, key);
            if (isMatch) {
                TcsmsMonitor monitor = map.getOrDefault(key, null);
                if (monitor != null && monitor.isPause()) {
                    monitor.awake();
                    log.info(key + "-------这个设备的监听器已经唤醒！");
                }
            }
        }
    }

    public void openMonitor() {

    }
}
