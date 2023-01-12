package com.example.usermanager.model;


import lombok.Data;

/**
 * 用户实体类
 */
@Data
public class UserInfo {
    private int uid;
    private String username;
    private String loginname;
    private String password;
    private String sex;
    private int age;
    private String address;
    private String qq;
    private String email;
    private boolean isadmin;
    private int state;
    private String createtime;
    private String updatetime;

}
