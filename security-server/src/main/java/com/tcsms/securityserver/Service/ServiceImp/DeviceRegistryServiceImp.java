package com.tcsms.securityserver.Service.ServiceImp;



import com.tcsms.securityserver.Dao.DeviceRegistryDao;
import com.tcsms.securityserver.Entity.DeviceRegistry;
import com.tcsms.securityserver.Service.DeviceRegistryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.tcsms.securityserver.Config.ConstantConfig.REGISTRY_KEY;


@Log4j2
@Service
public class DeviceRegistryServiceImp implements DeviceRegistryService {
    @Autowired
    DeviceRegistryDao deviceRegistryDao;
    @Autowired
    RedisServiceImp redisServiceImp;

    public int deviceRegister(String deviceId ,String isRegistered) {
        if (deviceRegistryDao.deviceRegister(deviceId, isRegistered) > 0) {
            redisServiceImp.getRedis().set(deviceId + REGISTRY_KEY, isRegistered);
            return 1;
        }
        return 0;
    }

    public int updateDeviceRegistry(DeviceRegistry deviceRegistry) {
        return deviceRegistryDao.update(deviceRegistry);
    }

}
