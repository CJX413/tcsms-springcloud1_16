package com.tcsms.securityserver.Controller;


import com.tcsms.securityserver.Monitor.MonitorManager;
import com.tcsms.securityserver.Service.ReceiveServiceImp.SecurityServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class SecurityController {

    @Autowired
    SecurityServiceImp securityServiceImp;


    @RequestMapping("/openSecuritySystem")
    public void openSecuritySystem() {
        securityServiceImp.openDeviceCollisionMonitor();
    }
    @RequestMapping("/closeSecuritySystem")
    public void closeSecuritySystem(){
        new MonitorManager().shutDownAllMonitor();
    }

}
