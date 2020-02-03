package com.tcsms.securityserver.Service.ServiceImp;

import com.tcsms.securityserver.Dao.DeviceRegistryDao;
import com.tcsms.securityserver.Dao.OperatorDao;
import com.tcsms.securityserver.Entity.DeviceRegistry;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Monitor.DeviceCollisionMonitor;
import com.tcsms.securityserver.Monitor.ManagerMonitor;
import com.tcsms.securityserver.Monitor.MonitorManager;
import com.tcsms.securityserver.Monitor.OtherMonitor;
import com.tcsms.securityserver.Service.SecurityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcsms.securityserver.Config.ConstantConfig.MANAGER_MONITOR_THREAD;
import static com.tcsms.securityserver.Config.ConstantConfig.REGISTERED;

@Log4j2
@Service
@Component
public class SecurityServiceImp implements SecurityService {
    @Autowired
    DeviceRegistryDao deviceRegistryDao;
    @Autowired
    OperatorDao operatorDao;
    @Autowired
    RedisServiceImp redisServiceImp;

    public void openDeviceCollisionMonitor() {
        List<DeviceRegistry> deviceRegistryList = deviceRegistryDao.findByIsRegistered(REGISTERED);
        List<OperationLog> operationLogList = new ArrayList<>();
        for (DeviceRegistry deviceRegistry : deviceRegistryList) {
            OperationLog operationLog = redisServiceImp.getOperationLogByDeviceRegistry(deviceRegistry);
            if (operationLog != null) {
                operationLogList.add(operationLog);
            }
        }
        for (int i = 0; i < operationLogList.size(); i++) {
            for (int j = i + 1; j < operationLogList.size(); j++) {
                String threadName = operationLogList.get(i).getDeviceId() + operationLogList.get(j).getDeviceId();
                DeviceCollisionMonitor monitor = new DeviceCollisionMonitor(operationLogList.get(i),
                        operationLogList.get(j), threadName);
                if (!monitor.isCompleteSafe()) {
                    monitor.start();
                    MonitorManager.addMonitor(monitor);
                }
            }
        }
    }

    public void openOtherMonitor() {
        List<DeviceRegistry> list = deviceRegistryDao.findByIsRegistered(REGISTERED);
        List<OperationLog> operationLogList = new ArrayList<>();
        for (DeviceRegistry deviceRegistry : list) {
            OperationLog operationLog = redisServiceImp.getOperationLogByDeviceRegistry(deviceRegistry);
            if (operationLog != null) {
                operationLogList.add(operationLog);
            }
        }
        Map<String, String> operatorMap = new HashMap<>();
        operatorDao.findAll().forEach(operator -> {
            operatorMap.put(operator.getWorkerId(), operator.getName());
        });
        operationLogList.forEach(operationLog -> {
                    String threadName = operationLog.getDeviceId();
                    OtherMonitor monitor = new OtherMonitor(operationLog, operatorMap, threadName);
                    monitor.start();
                    MonitorManager.addMonitor(monitor);
                }
        );
    }

    public void openManagerMonitor() {
        List<DeviceRegistry> deviceList = deviceRegistryDao.findByIsRegistered(REGISTERED);
        ManagerMonitor monitor = new ManagerMonitor(deviceList, MANAGER_MONITOR_THREAD);
        monitor.start();
        MonitorManager.addMonitor(monitor);
    }


    public void closeSecuritySystem() {

    }


}
