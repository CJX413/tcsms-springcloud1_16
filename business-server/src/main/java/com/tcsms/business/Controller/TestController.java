package com.tcsms.business.Controller;


import com.tcsms.business.Dao.DeviceRegistryDao;
import com.tcsms.business.Entity.DeviceRegistry;
import com.tcsms.business.Service.ReceiveServiceImp.DeviceRegistryServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class TestController {
    @Autowired
    DeviceRegistryServiceImp deviceRegistryServiceImp;
    @Autowired
    DeviceRegistryDao deviceRegistryDao;
    @RequestMapping("/test1")
    public String test1(@RequestParam("deviceId")String deviceId,
                        @RequestParam("isRegistered")String isRegistered){
        DeviceRegistry deviceRegistry=new DeviceRegistry();
        log.info(deviceId+isRegistered);
        deviceRegistry.setDeviceId(deviceId);
        deviceRegistry.setIsRegistered(isRegistered);
        deviceRegistryServiceImp.deviceRegister(deviceId,isRegistered);
        return "ok";
    }
    @RequestMapping("/test2")
    public String test2(){
        DeviceRegistry deviceRegistry=new DeviceRegistry();
        deviceRegistry.setDeviceId("1#");
        deviceRegistry.setLatitude(100000.000);
        deviceRegistry.setRlt(500.0);
        deviceRegistryDao.update(deviceRegistry);
        return "ok";
    }

}
