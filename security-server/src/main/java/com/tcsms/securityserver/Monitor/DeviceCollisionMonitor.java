package com.tcsms.securityserver.Monitor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.tcsms.securityserver.Config.ExceptionInfo;
import com.tcsms.securityserver.Config.WarningInfo;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Exception.MonitorException;
import com.tcsms.securityserver.JSON.ResultJSON;
import com.tcsms.securityserver.JSON.SendJSON;
import com.tcsms.securityserver.Service.ReceiveServiceImp.RedisServiceImp;
import com.tcsms.securityserver.Service.ReceiveServiceImp.RestTemplateServiceImp;
import com.tcsms.securityserver.Utils.SpringUtil;
import lombok.extern.log4j.Log4j2;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;
import redis.clients.jedis.Jedis;

import static com.tcsms.securityserver.Config.ConstantConfig.*;

@Log4j2
public class DeviceCollisionMonitor extends TcsmsMonitor {
    private OperationLog device_1;
    private OperationLog device_2;

    private Double bigHeight_1;
    private Double bigLength_1;

    private Double bigHeight_2;
    private Double bigLength_2;

    private JsonArray data;

    private RestTemplateServiceImp restTemplateService;
    private RedisServiceImp redisServiceImp;

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

    public boolean isYellowWarning() {
        double distance = getDistance();
        double bearing = getBearing();

        double Ox2 = distance * Math.cos((bearing / 180) * Math.PI);
        double Oy2 = distance * Math.sin((bearing / 180) * Math.PI);
        double r1 = bigLength_1 + SAFE_DISTANCE;
        double r2 = bigLength_2 + SAFE_DISTANCE;
        double x1 = device_1.getRadius() * Math.cos((device_1.getAngle() / 180) * Math.PI);
        double y1 = device_1.getRadius() * Math.sin((device_1.getAngle() / 180) * Math.PI);
        double x2 = device_2.getRadius() * Math.cos((device_2.getAngle() / 180) * Math.PI) + Ox2;
        double y2 = device_2.getRadius() * Math.sin((device_2.getAngle() / 180) * Math.PI) + Oy2;
        double p1toO2 = Math.sqrt((x1 - Ox2) * (x1 - Ox2) + (y1 - Oy2) * (y1 - Oy2));
        double p2toO1 = Math.sqrt(x2 * x2 + y2 * y2);
        log.info(device_1.getDeviceID() + "和" + device_2.getDeviceID() + ":" + String.valueOf(p1toO2 - r2) + "||" + String.valueOf(p2toO1 - r1));
        if (p1toO2 < r2 && p2toO1 < r1) {
            return true;
        } else if ((p1toO2 < r2 && p2toO1 > r1) && (bigHeight_1 > bigHeight_2)) {
            return true;
        } else if ((p1toO2 > r2 && p2toO1 < r1) && (bigHeight_1 < bigHeight_2)) {
            return true;
        }
        return false;
    }

    public boolean isRedWarning() {
        double distance = getDistance();
        double bearing = getBearing();
        double Ox2 = distance * Math.cos((bearing / 180) * Math.PI);
        double Oy2 = distance * Math.sin((bearing / 180) * Math.PI);
        double r1 = bigLength_1;
        double r2 = bigLength_2;
        double x1 = device_1.getRadius() * Math.cos((device_1.getAngle() / 180) * Math.PI);
        double y1 = device_1.getRadius() * Math.sin((device_1.getAngle() / 180) * Math.PI);
        double x2 = device_2.getRadius() * Math.cos((device_2.getAngle() / 180) * Math.PI) + Ox2;
        double y2 = device_2.getRadius() * Math.sin((device_2.getAngle() / 180) * Math.PI) + Oy2;
        double p1toO2 = Math.sqrt((x1 - Ox2) * (x1 - Ox2) + (y1 - Oy2) * (y1 - Oy2));
        double p2toO1 = Math.sqrt(x2 * x2 + y2 * y2);
        log.info(device_1.getDeviceID() + "和" + device_2.getDeviceID() + ":" + String.valueOf(p1toO2 - r2) + "||" + String.valueOf(p2toO1 - r1));
        if (p1toO2 < r2 && p2toO1 < r1) {
            return true;
        } else if ((p1toO2 < r2 && p2toO1 > r1) && (bigHeight_1 > bigHeight_2)) {
            return true;
        } else if ((p1toO2 > r2 && p2toO1 < r1) && (bigHeight_1 < bigHeight_2)) {
            return true;
        }
        return false;
    }

    public void run() {
        try {
            redisServiceImp = SpringUtil.getBean(RedisServiceImp.class);
            restTemplateService = SpringUtil.getBean(RestTemplateServiceImp.class);
            Jedis jedis = redisServiceImp.getRedis();
            Gson gson = new Gson();
            while (!Thread.interrupted()) {
                device_1 = gson.fromJson(jedis.get(device_1.getDeviceID()), OperationLog.class);
                device_2 = gson.fromJson(jedis.get(device_2.getDeviceID()), OperationLog.class);
                if (isRedWarning()) {
                    data = new JsonArray();
                    data.add(device_1.toString());
                    data.add(device_2.toString());
                    SendJSON json = new SendJSON(WarningInfo.DEVICE_COLLISION_RED_WARNING.getCode(),
                            WarningInfo.DEVICE_COLLISION_RED_WARNING.getMsg(), data);
                    ResultJSON result = gson.fromJson(restTemplateService.sendJson(WARNING_RECEIVE_URL, json.toString()),
                            ResultJSON.class);
                    if (result.getCode() != 200) {
                        throw new MonitorException(ExceptionInfo.DEVICE_COLLISION_MONITOR_SEND_WARNING.getCode(),
                                ExceptionInfo.DEVICE_COLLISION_MONITOR_SEND_WARNING.getMsg(), data);
                    }
                }
                if (isYellowWarning()) {
                    data = new JsonArray();
                    data.add(device_1.toString());
                    data.add(device_2.toString());
                    SendJSON json = new SendJSON(WarningInfo.DEVICE_COLLISION_YELLOW_WARNING.getCode(),
                            WarningInfo.DEVICE_COLLISION_YELLOW_WARNING.getMsg(), data);
                    ResultJSON result = gson.fromJson(restTemplateService.sendJson(WARNING_RECEIVE_URL, json.toString()),
                            ResultJSON.class);
                    if (result.getCode() != 200) {
                        throw new MonitorException(ExceptionInfo.DEVICE_COLLISION_MONITOR_SEND_WARNING.getCode(),
                                ExceptionInfo.DEVICE_COLLISION_MONITOR_SEND_WARNING.getMsg(), data);
                    }
                }
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            SendJSON exception = new SendJSON(ExceptionInfo.DEVICE_COLLISION_MONITOR_STOP.getCode(),
                    ExceptionInfo.DEVICE_COLLISION_MONITOR_STOP.getMsg(), data);
            restTemplateService.sendJson(ERROE_RECEIVE_URL, exception.toString());
        } catch (MonitorException e) {
            restTemplateService.sendJson(ERROE_RECEIVE_URL, e.toString());
        }
    }

}
