package com.tcsms.business.Controller;



import com.tcsms.business.Dao.UserDao;
import com.tcsms.business.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/register")
    public String registerUser(@RequestParam("username")String username, @RequestParam("password")String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRole("ROLE_USER");
        User save = userDao.save(user);
        return save.toString();
    }
    @RequestMapping("/home")
    public String home(@RequestParam("token") String token){
        return "/home?token="+token;
    }
}
