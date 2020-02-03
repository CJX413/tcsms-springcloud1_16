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
import lombok.extern.log4j.Log4j2;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

import static com.tcsms.securityserver.Config.ConstantConfig.ERROE_RECEIVE_URL;
import static com.tcsms.securityserver.Config.ConstantConfig.SAFE_DISTANCE;

@Log4j2
public class DeviceCollisionMonitor extends TcsmsMonitor {
    private OperationLog device_1;
    private OperationLog device_2;

    private Double bigHeight_1;
    private Double bigLength_1;

    private Double bigHeight_2;
    private Double bigLength_2;


    public DeviceCollisionMonitor(OperationLog device_1, OperationLog device_2, String threadName) {
        super(threadName);
        this.device_1 = device_1;
        this.device_2 = device_2;
        this.bigHeight_1 = device_1.getBigHeight();
        this.bigLength_1 = device_1.getBigLength();
        this.bigHeight_2 = device_2.getBigHeight();
        this.bigLength_2 = device_2.getBigLength();

    }

    public double getDistance() {
        GlobalCoordinates source = new GlobalCoordinates(device_1.getLatitude(), device_1.getLongitude());
        GlobalCoordinates target = new GlobalCoordinates(device_2.getLatitude(), device_2.getLongitude());
        GeodeticCalculator geodeticCalculator = new GeodeticCalculator();
        double istance = geodeticCalculator.calculateGeodeticCurve(Ellipsoid.Sphere, source, target).getEllipsoidalDistance();
        return istance;
    }

    public double getBearing() {
        double longitudeFrom = device_1.getLongitude();
        double longitudeTo = device_2.getLongitude();
        double latitudeFrom = Math.toRadians(device_1.getLatitude());
        double latitudeTo = Math.toRadians(device_2.getLatitude());
        double longDiff = Math.toRadians(longitudeTo - longitudeFrom);
        double y = Math.sin(longDiff) * Math.cos(latitudeTo);
        double x = Math.cos(latitudeFrom) * Math.sin(latitudeTo) - Math.sin(latitudeFrom) * Math.cos(latitudeTo) * Math.cos(longDiff);
        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }

    public boolean isCompleteSafe() {
        if (bigLength_1 + bigLength_2 < getDistance()) {
            return true;
        }
        return false;
    }

    @Override
    public JsonArray getData() {
        JsonArray data = new JsonArray();
        data.add(device_1.getJsonObject());
        data.add(device_2.getJsonObject());
        return data;
    }

    @Override
    public List<WarningInfo> isWarning() {
        List<WarningInfo> warning = new ArrayList<>();
        double distance = getDistance();
        double bearing = getBearing();
        double Ox2 = distance * Math.cos((bearing / 180) * Math.PI);
        double Oy2 = distance * Math.sin((bearing / 180) * Math.PI);
        double safe_r1 = bigLength_1 + SAFE_DISTANCE;
        double safe_r2 = bigLength_2 + SAFE_DISTANCE;
        double r1 = bigLength_1;
        double r2 = bigLength_2;
        double x1 = device_1.getRadius() * Math.cos((device_1.getAngle() / 180) * Math.PI);
        double y1 = device_1.getRadius() * Math.sin((device_1.getAngle() / 180) * Math.PI);
        double x2 = device_2.getRadius() * Math.cos((device_2.getAngle() / 180) * Math.PI) + Ox2;
        double y2 = device_2.getRadius() * Math.sin((device_2.getAngle() / 180) * Math.PI) + Oy2;
        double p1toO2 = Math.sqrt((x1 - Ox2) * (x1 - Ox2) + (y1 - Oy2) * (y1 - Oy2));
        double p2toO1 = Math.sqrt(x2 * x2 + y2 * y2);

        if (p1toO2 < safe_r2 && p2toO1 < safe_r1) {
            warning.add(WarningInfo.DEVICE_COLLISION_YELLOW_WARNING);
        } else if ((p1toO2 < safe_r2 && p2toO1 > safe_r1) && (bigHeight_1 > bigHeight_2)) {
            warning.add(WarningInfo.DEVICE_COLLISION_YELLOW_WARNING);
        } else if ((p1toO2 > safe_r2 && p2toO1 < safe_r1) && (bigHeight_1 < bigHeight_2)) {
            warning.add(WarningInfo.DEVICE_COLLISION_YELLOW_WARNING);
        }
        if (p1toO2 < r2 && p2toO1 < r1) {
            warning.add(WarningInfo.DEVICE_COLLISION_RED_WARNING);
        } else if ((p1toO2 < r2 && p2toO1 > r1) && (bigHeight_1 > bigHeight_2)) {
            warning.add(WarningInfo.DEVICE_COLLISION_RED_WARNING);
        } else if ((p1toO2 > r2 && p2toO1 < r1) && (bigHeight_1 < bigHeight_2)) {
            warning.add(WarningInfo.DEVICE_COLLISION_RED_WARNING);
        }
        return warning;
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                redisServiceImp = SpringUtil.getBean(RedisServiceImp.class);
                restTemplateService = SpringUtil.getBean(RestTemplateServiceImp.class);
                Jedis jedis = redisServiceImp.getRedis();
                Gson gson = new Gson();
                List<WarningInfo> isWarning;
                while (!Thread.interrupted()) {
                    wait(this);
                    log.info(device_1.getDeviceId() + device_2.getDeviceId() + "正在运行--------------");
                    device_1 = gson.fromJson(jedis.get(device_1.getDeviceId()), OperationLog.class);
                    device_2 = gson.fromJson(jedis.get(device_2.getDeviceId()), OperationLog.class);
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
                sendException(ExceptionInfo.DEVICE_COLLISION_MONITOR_STOP, data);
            } catch (SendWarningFailedException e) {
                JsonArray data = getData();
                sendException(ExceptionInfo.DEVICE_COLLISION_MONITOR_SEND_WARNING, data);
            }
        }
    }
}
