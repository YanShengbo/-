package com.example.usermanager.controller;

import cn.hutool.db.handler.StringHandler;
import com.example.usermanager.model.UserInfo;
import com.example.usermanager.service.UserService;
import com.example.usermanager.util.ConstVariable;
import com.example.usermanager.util.PasswordUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public boolean login(HttpServletRequest request, String loginname, String password){
        if(StringUtils.hasLength(loginname) && StringUtils.hasLength(password)){
            // 参数有效
            UserInfo userInfo = userService.getUserByLoginName(loginname);
            if(userInfo != null && userInfo.getUid() > 0) {
                String dbPassword = userInfo.getPassword();
                boolean result = PasswordUtil.decrypt(password,dbPassword);
                if(result == true){
                    //存储session
                    HttpSession session = request.getSession(true);
                    session.setAttribute(ConstVariable.USER_SEESION_KEY,userInfo);
                    return true;
                }
            }
        }
        return false;
    }

//
//    // 查询所有列表无分页
//    @RequestMapping("/listbypage")
//    public List<UserInfo> getAll(){
//        return userService.getAll();
//    }

    // 带分页和条件查询
    @RequestMapping("/listbypage")
    public HashMap<String,Object> getListByPage(String username,
                                                String address,
                                                String email,
                                                Integer pindex,
                                                Integer psize){
        HashMap<String,Object> result = new HashMap<>();
        if(pindex==null || pindex <1){
            pindex = 1;
        }
        if(psize==null || psize <=0){
            psize = 2;
        }
        if(!StringUtils.hasLength(username)){
            username = null;
        }
        if(!StringUtils.hasLength(address)){
            address = null;
        }
        if(!StringUtils.hasLength(email)){
            email = null;
        }
        int offset = (pindex-1)*psize;
        List<UserInfo> list = userService.getListByPage(username,address,email,psize,offset);
        int totalCount = userService.getListByPageCount(username,address,email);
        result.put("list",list);
        result.put("count",totalCount);
        return result;
    }

    // 添加用户
    @RequestMapping("/add")
    public int add(UserInfo userInfo,HttpServletRequest request){
        int result = 0;

        // 1.非空校验
        if(userInfo == null || !StringUtils.hasLength(userInfo.getUsername()) || !StringUtils.hasLength(userInfo.getLoginname())
        || !StringUtils.hasLength(userInfo.getPassword())){
            return result;
        }
        // 2.判断必须为超级管理员，才能进行添加操作
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute(ConstVariable.USER_SEESION_KEY)==null){
            return result;
        }
        UserInfo loginUser =(UserInfo) session.getAttribute(ConstVariable.USER_SEESION_KEY);
        if(!loginUser.isIsadmin()) {
            return result;
        }
        // 3.对登录名做一个唯一效应
        UserInfo loginNameUser = userService.getUserByLoginName(userInfo.getLoginname());
        // 登录名是否存在判断
        if(loginNameUser != null && loginNameUser.getUid() > 0){
            return result;
        }
        // 4.添加用户到数据库
        userInfo.setPassword(PasswordUtil.encrypt(userInfo.getPassword()));
        result = userService.add(userInfo);
        return result;
    }

    @RequestMapping("/getuserbyuid")
    public UserInfo getUserByUid(Integer uid){
        UserInfo userInfo = null;
        // 1.非空校验
        if(uid == null || uid <= 0){
            return userInfo;
        }
        // 2.查询数据库
        userInfo = userService.getUserByUid(uid);
        // 3.将密码隐藏掉
        userInfo.setPassword("");
        return userInfo;
    }

    @RequestMapping("/update")
    public int update(UserInfo userInfo,HttpServletRequest request){
        int result = 0;
        // 1.非空校验
        if(userInfo == null || !StringUtils.hasLength(userInfo.getUsername())
                || userInfo.getUid() <= 0){
            return result;
        }
        // 2.判断必须为超级管理员，才能进行添加操作
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute(ConstVariable.USER_SEESION_KEY)==null){
            return result;
        }
        UserInfo loginUser =(UserInfo) session.getAttribute(ConstVariable.USER_SEESION_KEY);
        if(!loginUser.isIsadmin()) {
            return result;
        }
        if(StringUtils.hasLength(userInfo.getPassword())){
            userInfo.setPassword(PasswordUtil.encrypt(userInfo.getPassword()));
        }
        // 4.添加用户到数据库
        result = userService.update(userInfo);

        return result;
    }

    @RequestMapping("/del")
    public int del(Integer uid){
        if(uid == null){
            return 0;
        }
        return userService.del(uid);
    }

    @RequestMapping("delbyids")
    public int dels(String ids,HttpServletRequest request){
        if(!StringUtils.hasLength(ids)){
            return 0;
        }
        String[] idsArr = ids.split(",");
        if(idsArr==null || idsArr.length<=0) return 0;
        List<Integer> idsList = new ArrayList<>();
        HttpSession session = request.getSession(false);
        if(session ==null || session.getAttribute(ConstVariable.USER_SEESION_KEY) == null) return 0;
        int uid = ((UserInfo)session.getAttribute(ConstVariable.USER_SEESION_KEY)).getUid();
        for(String item : idsArr){
            if(StringUtils.hasLength(item)){
                int thisUid = Integer.valueOf(item);
                if(uid==thisUid){
                    return 0;
                }
                idsList.add(thisUid);
            }
        }
        return userService.dels(idsList);
    }
}
