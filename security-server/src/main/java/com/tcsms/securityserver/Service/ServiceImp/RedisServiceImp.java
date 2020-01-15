package com.tcsms.securityserver.Service.ServiceImp;


import com.google.gson.Gson;
import com.tcsms.securityserver.Entity.DeviceRegistry;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisServiceImp implements RedisService {
    @Autowired
    JedisPool jedisPool;

    public Jedis getRedis() {
        return jedisPool.getResource();
    }

    public OperationLog getOperationLogByDeviceRegistry(DeviceRegistry deviceRegistry) {
        String json = jedisPool.getResource().get(deviceRegistry.getDeviceId());
        if (json == null) {
            return null;
        }
        OperationLog operationLog = new Gson().fromJson(json, OperationLog.class);
        operationLog.setRlt(deviceRegistry.getRlt());
        operationLog.setBigHeight(deviceRegistry.getBigHeight());
        operationLog.setSmallHeight(deviceRegistry.getSmallHeight());
        operationLog.setBigLength(deviceRegistry.getBigLength());
        operationLog.setSmallLength(deviceRegistry.getSmallLength());
        return operationLog;
    }
}
