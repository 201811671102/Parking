package com.cs.parking.service;

import com.cs.parking.pojo.User;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/17 20:18
 **/
public interface UserService {

    /*
    * 据openid查询用户信息
    * */
    User selectByOpenid(String openid);
    /*
    * 据主键查找
    * */
    User selectById(Integer id);
    /*
    * 添加用户信息
    * */
    void insertUser(User user);
    /*
    * 修改用户信息
    * */
    void updateUser(User user);
}
