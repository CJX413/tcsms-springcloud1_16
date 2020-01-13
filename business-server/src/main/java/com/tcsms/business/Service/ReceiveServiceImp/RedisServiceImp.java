package com.tcsms.business.Service.ReceiveServiceImp;

import com.tcsms.business.Service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisServiceImp implements RedisService {
    @Autowired
    JedisPool jedisPool;
    public Jedis getRedis(){
        return jedisPool.getResource();
    }
}
