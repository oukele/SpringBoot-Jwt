package com.example.oukele.controller;

import com.example.oukele.annotation.JwtIgnore;
import com.example.oukele.jwt.Audience;
import com.example.oukele.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping( path = "/login")
public class LoginController {

    @Autowired
    private Audience audience;

    /***
     *  模拟登陆
     * @param userNumber
     * @param password
     * @return
     */
    @PostMapping
    @JwtIgnore
    public String login(HttpServletResponse response, String userNumber, String password){

        String id = UUID.randomUUID().toString().replace("-","");
        String role = "admin";
        String token = JwtTokenUtil.createJWT(id, userNumber, role, audience);
        // 将token放在响应头
        response.setHeader(JwtTokenUtil.AUTH_HEADER_KEY, JwtTokenUtil.TOKEN_PREFIX + token);
        // 将token响应给客户端

        String msg = "{\"code\":200,\"token\":\""+ token +"\",\"msg\":\"登录成功\",\"flag\":true}";

        return msg;
    }

    @GetMapping()
    public List<String> userList() {
        System.out.println("### 查询所有用户列表 ###");
        List<String> list = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            list.add("第 "+ i +" 位 用户");
        }

        return list;
    }

}
