package com.quick.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.quick.constant.MessageConstant;
import com.quick.dto.UserLoginDTO;
import com.quick.entity.User;
import com.quick.exception.LoginFailedException;
import com.quick.mapper.UserMapper;
import com.quick.properties.WeChatProperties;
import com.quick.service.UserService;
import com.quick.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;
    /*
    微信登录
     */
    @Override
    public User WxLogin(UserLoginDTO userLoginDTO) {
        //调用微信的接口服务，获取微信用户的openid
        String openid = getOpenid(userLoginDTO.getCode());
        //判断openid是否为空，如果为空则登录失败，抛出异常
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //判断当前微信用户是否为新用户，如果是新用户，则自动完成注册
        User user = userMapper.getByOpenid(openid);
        if(user == null){
            user = user.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        return user;
    }
    /*
    获取微信用户openid
     */

    private String getOpenid(String code){
        //调用微信接口服务，获取微信用户的openid
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN_URL, map);
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
    }
}
