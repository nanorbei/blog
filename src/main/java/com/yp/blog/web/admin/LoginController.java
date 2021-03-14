package com.yp.blog.web.admin;

import com.yp.blog.pojo.User;
import com.yp.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    UserService userService;
    //进入登录页面
    @GetMapping
    public String loginPage() {
        return "admin/login";
    }
    //提交登录页面
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes attributes) {
        User user = userService.checkUser(username, password);
        if (user != null) {
            //我们将拿到的用户信息，保存在session中，之后可以在页面中显示用户信息，在此之前，我们最好不要将
            //密码保存在session中，不安全，考虑在保存用户信息前，将密码设置为null
            user.setPassword(null);
            session.setAttribute("user",user);
            return "admin/index";
        } else {
            //登录不成功的错误提示
            //因为后面我们采用的是重定向，所以我们这里不能使用Model
            attributes.addFlashAttribute("message", "用户名或密码错误");
            //我们这里要使用重定向，不然之后会有路径问题
            return "redirect:/admin";
        }
    }
    //提交登出
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        //将Session中的用户信息消除
        session.removeAttribute("user");
        return "redirect:/admin";
    }

}
