package com.tcsms.securityserver.Controller;


import com.google.gson.Gson;
import com.tcsms.securityserver.Config.ConstantConfig;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Service.ServiceImp.ReceiveServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;



@RestController
@Log4j2
public class ReceiveController {
    @Autowired
    ReceiveServiceImp receiveService;

    @RequestMapping(value = "/operationLog", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String receiveOperationLog(@RequestBody String json) {
        Gson gson = new Gson();
        OperationLog operationLog = gson.fromJson(json, OperationLog.class);
        BigDecimal radius = new BigDecimal(operationLog.getRadius());
        BigDecimal weight = new BigDecimal(operationLog.getWeight());
        BigDecimal torque = radius.multiply(weight).multiply(new BigDecimal(ConstantConfig.G));
        double Torque = torque.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        operationLog.setTorque(Torque);
        receiveService.receive(operationLog);
        return "0";
    }

}
