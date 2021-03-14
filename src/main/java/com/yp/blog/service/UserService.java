package com.yp.blog.service;

import com.yp.blog.pojo.User;

public interface UserService {
    //检查用户名和密码
    User checkUser(String username,String password);
}
