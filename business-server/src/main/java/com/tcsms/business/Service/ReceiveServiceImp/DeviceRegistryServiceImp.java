package com.tcsms.business.Service.ReceiveServiceImp;


import com.google.gson.JsonArray;
import com.tcsms.business.Dao.DeviceRegistryDao;
import com.tcsms.business.Entity.DeviceRegistry;
import com.tcsms.business.Service.DeviceRegistryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tcsms.business.Config.ConstantConfig.REGISTRY_KEY;


@Log4j2
@Service
public class DeviceRegistryServiceImp implements DeviceRegistryService {
    @Autowired
    DeviceRegistryDao deviceRegistryDao;
    @Autowired
    RedisServiceImp redisServiceImp;

    public int deviceRegister(String deviceId, String isRegistered) {
        if (deviceRegistryDao.deviceRegister(deviceId,isRegistered) > 0) {
            redisServiceImp.getRedis().set(deviceId + REGISTRY_KEY, isRegistered);
            return 1;
        }
        return 0;
    }

    public int updateDeviceRegistry(DeviceRegistry deviceRegistry) {
        return deviceRegistryDao.update(deviceRegistry);
    }
    public JsonArray getAllDeviceInfo(){
        JsonArray jsonArray=new JsonArray();
        deviceRegistryDao.findAll().forEach(deviceRegistry -> {
            jsonArray.add(deviceRegistry.getJsonObject());
        });
        return jsonArray;
    }
}
