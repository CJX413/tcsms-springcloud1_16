package com.tcsms.business.Controller;

import com.google.gson.JsonArray;
import com.tcsms.business.Entity.DeviceRegistry;
import com.tcsms.business.Service.ReceiveServiceImp.DeviceRegistryServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class DeviceController {
    @Autowired
    DeviceRegistryServiceImp deviceRegistryServiceImp;

    @RequestMapping("/deviceInfo")
    public String getDeviceInfo(){
        JsonArray jsonArray =deviceRegistryServiceImp.getAllDeviceInfo();
        log.info(jsonArray);
        return jsonArray.toString();
    }
}
