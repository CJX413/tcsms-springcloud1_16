package com.tcsms.securityserver.AOP;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.tcsms.securityserver.Dao.OperationLogDao;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.JSON.SendJSON;
import com.tcsms.securityserver.Monitor.MonitorManager;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
public class SendExceptionAop {
    @Autowired
    OperationLogDao operationLogDao;

    @Pointcut("execution(* com.tcsms.securityserver.Service.ServiceImp.RestTemplateServiceImp.sendException(..))")
    public void sendException() {
    }


    @Around(value = "sendException()")
    public Object exceptionLog(ProceedingJoinPoint point) throws Throwable {
        SendJSON json = (SendJSON) point.proceed();
        JsonArray datas = (JsonArray) json.getData();
        datas.forEach(data -> {
            OperationLog operationLog = new Gson().fromJson(data, OperationLog.class);
            MonitorManager.addWarningCount(operationLog.getDeviceId());
        });
        return point.proceed();
    }
}
