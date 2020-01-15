package com.tcsms.securityserver.Service.ServiceImp;

import com.tcsms.securityserver.Dao.DeviceRegistryDao;
import com.tcsms.securityserver.Dao.OperatorDao;
import com.tcsms.securityserver.Entity.DeviceRegistry;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Monitor.DeviceCollisionMonitor;
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

import static com.tcsms.securityserver.Config.ConstantConfig.*;

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
                String threadName = DeviceCollisionMonitor_THREAD_PREFIX +
                        operationLogList.get(i).getDeviceId() + operationLogList.get(j).getDeviceId();
                DeviceCollisionMonitor monitor = new DeviceCollisionMonitor(operationLogList.get(i),
                        operationLogList.get(j), threadName);
                if (!monitor.isCompleteSafe()) {
                    Thread thread = new Thread(monitor, monitor.getThreadName());
                    thread.start();
                    MonitorManager.addMonitor(thread);
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
        Map<String,String> operatorMap=new HashMap<>();
        operatorDao.findAll().forEach(operator -> {
            operatorMap.put(operator.getSpecialOperationCertificateNumber(),operator.getName());
        });
        operationLogList.forEach(operationLog -> {
                    String threadName = OtherMonitor_THREAD_PREFIX + operationLog.getDeviceId();
                    Thread thread = new Thread(new OtherMonitor(operationLog, operatorMap, threadName), threadName);
                    thread.start();
                    MonitorManager.addMonitor(thread);
                }
        );
    }


    public void closeSecuritySystem() {

    }


}
