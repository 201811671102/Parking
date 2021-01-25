package com.cs.parking.service.impl;

import com.cs.parking.code.BaseCode;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.mapper.UserMapper;
import com.cs.parking.pojo.User;
import com.cs.parking.pojo.UserExample;
import com.cs.parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/17 20:18
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserExample userExample;

    @Override
    public User selectByOpenid(String openid) {
        userExample.clear();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andOpenIdEqualTo(openid);
        List<User> users = userMapper.selectByExample(userExample);
        if (users != null && users.size()>0){
            return users.get(0);
        }
        return null;
    }

    @Override
    public User selectById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void insertUser(User user) {
        int i = userMapper.insertSelective(user);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"新增用户失败! ");
        }
    }

    @Override
    public void updateUser(User user) {
        int i = userMapper.updateByPrimaryKeySelective(user);
        if (i == -1){
            throw new ErrorException(BaseCode.System_Error,"用户实名认证失败! ");
        }
    }
}
