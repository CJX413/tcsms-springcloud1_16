package com.tcsms.business.Controller;


import com.tcsms.business.Dao.UserDao;
import com.tcsms.business.Entity.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class AuthController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/auth/register")
    public String register(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRole("ROLE_ADMIN");
        User save = userDao.save(user);
        return save.toString();
    }

    @RequestMapping("isLogin")
    public void isLogin(){ }
    @RequestMapping("/auth/home")
    public String home(@RequestParam("token") String token) {
        return "/home?token=" + token;
    }
    @RequestMapping("/login")
    public void login(@RequestParam("username") String username, @RequestParam("password") String password) {
        log.info(username+"-"+password);
    }
}
