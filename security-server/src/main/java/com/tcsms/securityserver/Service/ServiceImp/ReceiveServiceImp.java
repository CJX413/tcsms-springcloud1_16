package com.tcsms.securityserver.Service.ServiceImp;

import com.tcsms.securityserver.Dao.OperationLogDao;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Service.ReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiveServiceImp implements ReceiveService {
    @Autowired
    private OperationLogDao operationLogDao;
    @Autowired
    private RedisServiceImp redisServiceImp;
    public void receive(OperationLog operationLog) {
        redisServiceImp.getRedis().set(operationLog.getDeviceId(), operationLog.toString());
        //operationLogDao.save(operationLog);
    }
}
