package com.tcsms.business.AOP;



import com.tcsms.business.Dao.DeviceRegistryDao;
import com.tcsms.business.Service.ReceiveServiceImp.RedisServiceImp;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

//@Aspect
//@Component
@Log4j2
public class ReceiveControllerAop {
    @Autowired
    private RedisServiceImp redisServiceImp;
    @Autowired
    private DeviceRegistryDao deviceRegistryDao;
    private static String KEY = "_registry";

    @Pointcut("execution(public * com.example.tcsms.Controller.ReceiveController.receiveData(..))")
    public void receivePointCut() {
    }

    @Before("receivePointCut()")
    public void deoBefore(JoinPoint joinPoint) {
        log.info("方法执行前...");

        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        log.info("url:" + request.getRequestURI());
        log.info("ip:" + request.getRemoteHost());
        log.info("method:" + request.getMethod());
        log.info("class_method:" + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName());
        log.info("args:" + joinPoint.getArgs());
    }

    @After("receivePointCut()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("方法执行后...");
    }

    @Around("receivePointCut()")  //前置通知
    public Object beforeExcution(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
    }


}
