package com.quick.service;

import com.quick.dto.UserLoginDTO;
import com.quick.entity.User;

public interface UserService {

    /*
    微信登录
      * @param userLoginDTO
      * @return
     */

    User WxLogin(UserLoginDTO userLoginDTO);
}
