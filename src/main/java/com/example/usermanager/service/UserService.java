package com.example.usermanager.service;

import com.example.usermanager.mapper.UserMapper;
import com.example.usermanager.model.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public UserInfo login(String loginname, String password){
        return userMapper.login(loginname,password);
    }

    public List<UserInfo> getAll(){
        return userMapper.getAll();
    }

    public int add(UserInfo userInfo){
        return userMapper.add(userInfo);
    }

    public UserInfo getUserByLoginName(String loginname){
        return userMapper.getUserByLoginName(loginname);
    }

    public UserInfo getUserByUid(Integer uid){
        return userMapper.getUserByUid(uid);
    }

    public int update(UserInfo userInfo){
        return userMapper.update(userInfo);
    }

    public int del(Integer uid){
        return userMapper.del(uid);
    }

    public int dels(List<Integer> ids){
        return userMapper.dels(ids);
    }

    public List<UserInfo> getListByPage(String username,
                                        String address,
                                        String email,
                                        Integer limit,
                                        Integer offset){
        return userMapper.getListByPage(username,address,email,limit,offset);

    }
    
    public int getListByPageCount(String username,String address,String email){
        return userMapper.getListByPageCount(username,address,email);
    }
}
