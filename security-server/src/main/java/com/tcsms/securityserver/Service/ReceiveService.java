package com.tcsms.securityserver.Service;


import com.tcsms.securityserver.Entity.OperationLog;

public interface ReceiveService {
    void receive(OperationLog receiveEntity);
}
