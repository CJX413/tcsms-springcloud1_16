package com.tcsms.securityserver.Controller;


import com.tcsms.securityserver.Monitor.MonitorManager;
import com.tcsms.securityserver.Service.ServiceImp.SecurityServiceImp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class SecurityController {

    @Autowired
    SecurityServiceImp securityServiceImp;


    @RequestMapping("/securitySystemSwitch")
    public String securitySystemSwitch() {
        if (MonitorManager.turn_on) {
            MonitorManager.shutDownAllMonitor();
            MonitorManager.turn_on = false;
            return "安全系统已关闭";
        } else {
            securityServiceImp.openManagerMonitor();
            securityServiceImp.openDeviceCollisionMonitor();
            securityServiceImp.openOtherMonitor();
            MonitorManager.turn_on = true;
            return "安全系统已开启";
        }
    }
    @RequestMapping("/notifyMonitor")
    public String notifyMonitor(){
        MonitorManager.notifyAllMonitor();
        return "唤醒所有监听器";
    }

}
