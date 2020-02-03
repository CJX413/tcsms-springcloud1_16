package com.tcsms.business;

import com.tcsms.business.Dao.UserDao;
import com.tcsms.business.JSON.LoginJSON;
import com.tcsms.business.Service.ReceiveServiceImp.UserDetailsServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BusinessServerApplicationTests {

    @Autowired
    UserDao dao;
    @Test
    void contextLoads() {
        LoginJSON json=new LoginJSON();
        json.setCode(200);
        json.setSuccess(true);
        json.setToken("12312313213");
        json.setMassege("Login success");
        System.out.println(json.toString());
    }

}
