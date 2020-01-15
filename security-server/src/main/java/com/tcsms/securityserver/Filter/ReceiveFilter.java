package com.tcsms.securityserver.Filter;

import com.google.gson.Gson;

import com.tcsms.securityserver.Dao.DeviceRegistryDao;
import com.tcsms.securityserver.Entity.DeviceRegistry;
import com.tcsms.securityserver.Entity.OperationLog;
import com.tcsms.securityserver.Service.ServiceImp.RedisServiceImp;
import com.tcsms.securityserver.Utils.RequestReaderHttpServletRequestWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static com.tcsms.securityserver.Config.ConstantConfig.REGISTERED;
import static com.tcsms.securityserver.Config.ConstantConfig.REGISTRY_KEY;
import static com.tcsms.securityserver.Config.ConstantConfig.UN_REGISTERED;


@Log4j2
@WebFilter(filterName = "ReceiveFilter", urlPatterns = {"/json"})
public class ReceiveFilter implements Filter {
    private List<DeviceRegistry> list;
    @Autowired
    private DeviceRegistryDao deviceRegistryDao;
    @Autowired
    private RedisServiceImp redisServiceImp;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("----------------------->过滤器被创建");
        this.list = deviceRegistryDao.findAll();
        for (DeviceRegistry deviceRegistry : list) {
            redisServiceImp.getRedis().set(deviceRegistry.getDeviceId() + REGISTRY_KEY, deviceRegistry.getIsRegistered());
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        RequestReaderHttpServletRequestWrapper requestWrapper = new RequestReaderHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        String requestURI = requestWrapper.getRequestURI();
        System.out.println("--------------------->过滤器：请求地址" + requestURI);
        String json = getJson(requestWrapper);
        OperationLog operationLog = new Gson().fromJson(json, OperationLog.class);
        String value = redisServiceImp.getRedis().get(operationLog.getDeviceId() + REGISTRY_KEY);
        if (value == null) {
            DeviceRegistry deviceRegistry = new DeviceRegistry();
            deviceRegistry.setDeviceModel(operationLog.getDeviceModel());
            deviceRegistry.setIsRegistered(UN_REGISTERED);
            deviceRegistry.setDeviceId(operationLog.getDeviceId());
            deviceRegistry.setLatitude(operationLog.getLatitude());
            deviceRegistry.setLongitude(operationLog.getLongitude());
            redisServiceImp.getRedis().set(operationLog.getDeviceId() + REGISTRY_KEY, UN_REGISTERED);
            deviceRegistryDao.save(deviceRegistry);
        } else {
            if (value.equals(REGISTERED)) {
                filterChain.doFilter(requestWrapper, servletResponse);
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("----------------------->过滤器被销毁");
    }

    private String getJson(HttpServletRequestWrapper req) {
        try {
            try (BufferedReader reader = req.getReader()) {
                StringBuilder json = new StringBuilder();
                String s;
                while ((s = reader.readLine()) != null) {
                    json.append(s.trim());
                }
                return json.toString();
            }
        } catch (IOException e) {
            return null;
        }
    }
}
