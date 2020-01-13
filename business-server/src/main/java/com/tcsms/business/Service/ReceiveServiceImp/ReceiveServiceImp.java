package com.tcsms.business.Service.ReceiveServiceImp;

import com.tcsms.business.Dao.OperationLogDao;
import com.tcsms.business.Entity.OperationLog;
import com.tcsms.business.Service.ReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiveServiceImp implements ReceiveService {
    @Autowired
    private OperationLogDao operationLogDao;
    @Autowired
    private RedisServiceImp redisServiceImp;
    public void receive(OperationLog operationLog) {
        redisServiceImp.getRedis().set(operationLog.getDeviceID(), operationLog.toString());
        operationLogDao.save(operationLog);
    }
}
