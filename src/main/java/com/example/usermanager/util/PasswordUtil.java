package com.example.usermanager.util;


import cn.hutool.core.lang.Range;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.util.StringUtils;

import java.security.Security;

/**
 * 密码工具类
 * 1、加密
 * 2、解密
 */
public class PasswordUtil {
    // 加密
    public static String encrypt(String password){

        // 随机盐值
        String salt = IdUtil.simpleUUID();
        // 密码（md5(随即盐值+密码)）
        String finalPassword = SecureUtil.md5(salt+password);
        return salt+"$"+finalPassword;
    }
    // 解密
    public static boolean decrypt(String password, String securePassword){
        boolean result = false;
        if(StringUtils.hasLength(password) && StringUtils.hasLength(securePassword)){
            if(securePassword.length() == 65 && securePassword.contains("$")){
                String[] securePasswordArr = securePassword.split("\\$");
                String salt = securePasswordArr[0];
                String finalPassword = securePasswordArr[1];
                // 使用同样的加密算法和随机盐值生成最终的加密的密码
                password = SecureUtil.md5(salt+password);
                if(password.equals(finalPassword)){
                    result = true;

                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String password = "123";
        String dbPassword = PasswordUtil.encrypt(password);
        System.out.println(dbPassword);
        System.out.println(PasswordUtil.decrypt("123",dbPassword));
    }
}
