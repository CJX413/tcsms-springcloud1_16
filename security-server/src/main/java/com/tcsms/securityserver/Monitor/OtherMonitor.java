package com.tcsms.securityserver.Monitor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.tcsms.securityserver.Config.ExceptionInfo;
import com.tcsms.securityserver.Config.WarningInfo;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Exception.SendWarningFailedException;
import com.tcsms.securityserver.JSON.SendJSON;
import com.tcsms.securityserver.Service.ServiceImp.RedisServiceImp;
import com.tcsms.securityserver.Service.ServiceImp.RestTemplateServiceImp;
import com.tcsms.securityserver.Utils.SpringUtil;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tcsms.securityserver.Config.ConstantConfig.*;

public class OtherMonitor extends TcsmsMonitor {

    private OperationLog device;
    private Double rlt;
    private Map<String, String> operator;

    public OtherMonitor(OperationLog device, Map<String, String> operator, String threadName) {
        super(threadName);
        this.device = device;
        this.rlt = device.getRlt();
        this.operator = operator;
    }

    @Override
    public void run() {
        try {
            redisServiceImp = SpringUtil.getBean(RedisServiceImp.class);
            restTemplateService = SpringUtil.getBean(RestTemplateServiceImp.class);
            Jedis jedis = redisServiceImp.getRedis();
            Gson gson = new Gson();
            List<WarningInfo> isWarning;
            while (!Thread.interrupted()) {
                device = gson.fromJson(jedis.get(device.getDeviceId()), OperationLog.class);
                isWarning = isWarning();
                if (!isWarning.isEmpty()) {
                    for (WarningInfo warningInfo : isWarning) {
                        sendWarning(warningInfo, getData());
                    }
                }
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            JsonArray data = getData();
            SendJSON exception = new SendJSON(ExceptionInfo.OTHER_MONITOR_STOP.getCode(),
                    ExceptionInfo.OTHER_MONITOR_STOP.getMsg(), data);
            sendException(exception.toString());
        } catch (SendWarningFailedException e) {
            JsonArray data = getData();
            SendJSON exception = new SendJSON(ExceptionInfo.OTHER_MONITOR_SEND_WARNING.getCode(),
                    ExceptionInfo.OTHER_MONITOR_SEND_WARNING.getMsg(), data);
            sendException(exception.toString());
        }

    }

    @Override
    public JsonArray getData() {
        JsonArray data = new JsonArray();
        data.add(device.toString());
        return data;
    }

    @Override
    public List<WarningInfo> isWarning() {
        List<WarningInfo> warning = new ArrayList<>();
        String name = operator.getOrDefault(device.getSpecialOperationCertificateNumber(), "");
        if (device.getTorque() / rlt > SAFE_TORQUE) {
            warning.add(WarningInfo.TORQUE_YELLOW_WARNING);
        }
        if (device.getTorque() > rlt) {
            warning.add(WarningInfo.TORQUE_RED_WARNING);
        }
        if (device.getWindVelocity() >= MODERATE_BREEZE) {
            warning.add(WarningInfo.WIND_VELOCITY_YELLOW_WARNING);
        }
        if (device.getWindVelocity() >= STRONG_BREEZE) {
            warning.add(WarningInfo.WIND_VELOCITY_RED_WARNING);
        }
        if (!device.getOperator().equals(name)) {
            warning.add(WarningInfo.OPERATOR_RED_WARNING);
        }
        return warning;
    }

}
