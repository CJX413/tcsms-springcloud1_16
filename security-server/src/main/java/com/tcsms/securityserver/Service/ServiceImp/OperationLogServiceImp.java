package com.tcsms.securityserver.Service.ServiceImp;

import com.tcsms.securityserver.Dao.OperationLogDao;
import com.tcsms.securityserver.Service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationLogServiceImp implements OperationLogService {

    @Autowired
    OperationLogDao operationLogDao;

}
