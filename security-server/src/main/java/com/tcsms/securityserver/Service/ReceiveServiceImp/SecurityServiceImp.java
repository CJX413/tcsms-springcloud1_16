package com.tcsms.securityserver.Service.ReceiveServiceImp;

import com.google.gson.Gson;
import com.tcsms.securityserver.Dao.DeviceRegistryDao;
import com.tcsms.securityserver.Entity.DeviceRegistry;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Monitor.DeviceCollisionMonitor;
import com.tcsms.securityserver.Monitor.MonitorManager;
import com.tcsms.securityserver.Service.SecurityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.tcsms.securityserver.Config.ConstantConfig.REGISTERED;
import static com.tcsms.securityserver.Config.ConstantConfig.SECURITY_THREAD_PREFIX;

@Log4j2
@Service
@Component
public class SecurityServiceImp implements SecurityService {
    @Autowired
    DeviceRegistryDao deviceRegistryDao;
    @Autowired
    RedisServiceImp redisServiceImp;

    public void openDeviceCollisionMonitor() {
        List<DeviceRegistry> list = deviceRegistryDao.findByIsRegistered(REGISTERED);
        List<OperationLog> operationLogList = new ArrayList<>();
        for (DeviceRegistry deviceRegistry : list) {
            String json = redisServiceImp.getRedis().get(deviceRegistry.getDeviceId());
            if (json != null) {
                OperationLog operationLog = new Gson().fromJson(json, OperationLog.class);
                operationLog.setBigHeight(deviceRegistry.getBigHeight());
                operationLog.setSmallHeight(deviceRegistry.getSmallHeight());
                operationLog.setBigLength(deviceRegistry.getBigLength());
                operationLog.setSmallLength(deviceRegistry.getSmallLength());
                operationLogList.add(operationLog);
            }
        }
        for (int i = 0; i < operationLogList.size(); i++) {
            for (int j = i + 1; j < operationLogList.size(); j++) {
                String threadName = SECURITY_THREAD_PREFIX +
                        operationLogList.get(i).getDeviceID() + operationLogList.get(j).getDeviceID();
                DeviceCollisionMonitor monitor = new DeviceCollisionMonitor(operationLogList.get(i),
                        operationLogList.get(j), threadName);
                if (!monitor.isCompleteSafe()) {
                    log.info(operationLogList.get(i).getDeviceID()
                            + "和" + operationLogList.get(j).getDeviceID() + "之间开启碰撞监控！");
                    Thread thread = new Thread(monitor, monitor.getThreadName());
                    thread.start();
                    new MonitorManager().addMonitor(thread);
                }
            }
        }
    }


    public void closeSecuritySystem() {

    }


}
