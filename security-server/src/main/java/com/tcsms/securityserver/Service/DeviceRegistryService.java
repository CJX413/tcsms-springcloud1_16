package com.tcsms.securityserver.Service;


import com.tcsms.securityserver.Entity.DeviceRegistry;

public interface DeviceRegistryService {
    int deviceRegister(String deviceId ,String isRegistered);
    int updateDeviceRegistry(DeviceRegistry deviceRegistry);
}
