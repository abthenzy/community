package com.yufeng.community.demo.controller;


import com.yufeng.community.demo.dto.AccessTokenDTO;
import com.yufeng.community.demo.dto.GithubUser;
import com.yufeng.community.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    //利用配置文件将代码配置分离
   /*

    @Value("${github.redirect.uri}")
    private String redirectUri;*/
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Value("${github.client.secret}")
    private String secret;


    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_secret(secret);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setRedirect_uri(redirectUri);
        String accessToken= githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
       // System.out.println(githubUser.getName());
        if(githubUser !=null){
            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
            //登录成功，写cookie,和session
        }else {
            //登录失败，重新登录
            return "redirect:/";
        }
        //return "index";
    }
}
