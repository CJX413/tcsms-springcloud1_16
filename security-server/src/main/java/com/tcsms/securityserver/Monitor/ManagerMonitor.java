package com.tcsms.securityserver.Monitor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.tcsms.securityserver.Config.WarningInfo;
import com.tcsms.securityserver.Entity.DeviceRegistry;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Service.ServiceImp.RedisServiceImp;
import com.tcsms.securityserver.Service.ServiceImp.RestTemplateServiceImp;
import com.tcsms.securityserver.Utils.SpringUtil;
import redis.clients.jedis.Jedis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcsms.securityserver.Config.ConstantConfig.ALLOWED_NOT_RUNNING_TIME;
import static com.tcsms.securityserver.Config.ConstantConfig.MODERATE_BREEZE;

public class ManagerMonitor extends TcsmsMonitor {
    private List<DeviceRegistry> deviceList;
    private Map<String, Map<String, Long>> comparator = new HashMap<>();

    public ManagerMonitor(List<DeviceRegistry> deviceList, String threadName) {
        super(threadName);
        this.deviceList = deviceList;
    }

    @Override
    public JsonArray getData() {
        return null;
    }

    @Override
    public List<WarningInfo> isWarning() {
        return null;
    }

    @Override
    public void run() {
        try {
            redisServiceImp = SpringUtil.getBean(RedisServiceImp.class);
            restTemplateService = SpringUtil.getBean(RestTemplateServiceImp.class);
            Jedis jedis = redisServiceImp.getRedis();
            Gson gson = new Gson();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while (Thread.interrupted()) {
                for (DeviceRegistry device : deviceList) {
                    OperationLog operationLog = gson.fromJson(jedis.get(device.getDeviceId()), OperationLog.class);
                    long compareCode = operationLog.getHeight().hashCode() + operationLog.getOperator().hashCode() +
                            operationLog.getRadius().hashCode() +
                            operationLog.getSpecialOperationCertificateNumber().hashCode() +
                            operationLog.getTorque().hashCode() + operationLog.getAngle().hashCode() +
                            MonitorManager.getWarningCount(operationLog.getDeviceId());
                    Map<String, Long> value = comparator.getOrDefault(operationLog.getDeviceId(), null);
                    long now = formatter.parse(operationLog.getTime()).getTime();
                    if (value == null) {
                        Map<String, Long> map = new HashMap<>();
                        map.put("compareCode", compareCode);
                        map.put("pauseDate", now);
                        comparator.put(operationLog.getDeviceId(), map);
                    } else if (value.get("compareCode") == compareCode && operationLog.getWindVelocity() < MODERATE_BREEZE) {
                        //如果设备有30分钟没有在运行、没有发出过警报、风速不到4级黄色预警就让设备暂停监控，等之后重新运行后开启；
                    } else if (value.get("pauseDate") < now) {
                        MonitorManager.pauseMonitorByName(operationLog.getDeviceId());
                    } else {
                        long pauseDate = now + ALLOWED_NOT_RUNNING_TIME;
                        value.put("pauseDate", pauseDate);
                        comparator.put(operationLog.getDeviceId(), value);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
