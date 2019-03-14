package com.taotao.sso.controller;

import com.taotao.common.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class PageController {
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;
    @RequestMapping("/page/register")
    public String showRegister(){
        return "register";
    }

    @RequestMapping("/page/login")
    public String showLogin(String url, Model model, HttpServletRequest request, HttpServletResponse response){
        //TODO 获取在/user/login中在jedis中的值
        String cookieValue = CookieUtils.getCookieValue(request, TOKEN_KEY);
        model.addAttribute("redirect", url);
        return "login";
    }
}
