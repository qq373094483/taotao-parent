package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @RequestMapping("check/{param}/{type}")
    @ResponseBody
    public TaotaoResult checkUserData(@PathVariable String param, @PathVariable Integer type) {
        return userService.checkData(param, type);
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser tbUser) {
        return userService.register(tbUser);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String username, String password, HttpServletResponse response, HttpServletRequest request) {
        TaotaoResult result = userService.login(username, password);
        if (result.getStatus() == 200) {
            CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
        }
        return result;
    }

    @RequestMapping(value = "logout")
    public String logout(HttpServletRequest request,HttpServletResponse response) {
        String referer = request.getHeader("Referer");
        CookieUtils.deleteCookie(request,response,TOKEN_KEY);
        return "redirect:"+referer;
    }

    /**
     *
     * @param token
     * @param callback
     * @return
     */
    /*@RequestMapping(value = "token/{token}", method = RequestMethod.GET,
    //默认返回的string的content-type是text/plain，指定返回响应数据的content-type
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseBody
    public String getUserByToken(@PathVariable String token,String callback) {
        TaotaoResult result = userService.getUserByToken(token);
        //不为空就是jsonp请求
        if (StringUtils.isNotBlank(callback)) {
            return callback + "(" + JsonUtils.objectToJson(result) + ");";
        }
        return JsonUtils.objectToJson(result);
    }

    @RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET,
            //默认返回的string的content-type是text/plain，指定返回响应数据的content-type
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )*/
    //jsonp的第二种方法，spring4.1及以上
    @RequestMapping(value = "token/{token}", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserByToken(@PathVariable String token,String callback) {
        TaotaoResult result = userService.getUserByToken(token);
        //不为空就是jsonp请求
        if (StringUtils.isNotBlank(callback)) {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }
}

