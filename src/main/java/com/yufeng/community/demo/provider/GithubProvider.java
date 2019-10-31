package com.yufeng.community.demo.provider;

import com.yufeng.community.demo.dto.AccessTokenDTO;
import com.yufeng.community.demo.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import com.alibaba.fastjson.*;


/**
 *
 */
@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
       MediaType mediaType
                = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try {
            try (Response response = client.newCall(request).execute()) {
                String string= response.body().string();
                String token=string.split("&")[0].split("=")[1];
                return token;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string =response.body().string();
             GithubUser gUser= JSON.parseObject(string,GithubUser.class);
             return gUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
