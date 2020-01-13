package com.tcsms.business.Controller;


import com.tcsms.business.JSON.ResultJSON;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Log4j2
public class ReceiveController {

    @RequestMapping(value = "/warning", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String receiveWarning(@RequestBody String json) {
        log.info(json);
        return new ResultJSON(200,true,"接收到报警信号！",null).toString();
    }
    @RequestMapping(value = "/error", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String receiveError(@RequestBody String json) {
        log.info(json);
        return new ResultJSON(200,true,"接收到报警信号！",null).toString();
    }
}
