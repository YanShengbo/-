package com.example.usermanager.mapper;

import com.example.usermanager.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.swing.*;
import java.util.List;

@Mapper
public interface UserMapper {
    // 登录功能
    UserInfo login(@Param("loginname")String loginname,
                   @Param("password")String password);

    // 查询所有用户的信息
    List<UserInfo> getAll();

    // 添加用户
    int add(UserInfo userInfo);

    // 根据登录名查询用户信息
    UserInfo getUserByLoginName(@Param("loginname") String loginname);

    // 根据 uid 查询用户信息
    UserInfo getUserByUid(@Param("uid")Integer uid);

    // 修改
    int update(UserInfo userInfo);

    // 删除
    int del(@Param("uid")Integer uid);

    // 多条删除
    int dels(List<Integer> ids);

    // 分页显示 list
    List<UserInfo> getListByPage(@Param("username")String username,
                                 @Param("address")String address,
                                 @Param("email")String email,
                                 @Param("limit")Integer limit,
                                 @Param("offset")Integer offset);

    int getListByPageCount(@Param("username")String username,
                           @Param("address")String address,
                           @Param("email")String email);

}
