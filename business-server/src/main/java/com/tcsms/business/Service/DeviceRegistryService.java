package com.tcsms.business.Service;


import com.tcsms.business.Entity.DeviceRegistry;

public interface DeviceRegistryService {
    int deviceRegister(String deviceId, String isRegistered);
    int updateDeviceRegistry(DeviceRegistry deviceRegistry);
}
