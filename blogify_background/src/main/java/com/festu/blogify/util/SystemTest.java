package com.festu.blogify.util;

import com.festu.blogify.realm.LoginRealm;
import com.festu.blogify.service.OperationService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
@EnableConfigurationProperties
public class SystemTest {
    @Value("test")
    String path;

    @Autowired
    OperationService operationService;

    @Test
    public void getPath(){
        System.out.println(this.path);
    }

    @Test
    public void DateFormatTest(){
        String ldt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(ldt);
    }
    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Test // 在方法开始前添加一个用户,让它具备admin和user两个角色
    public void addUser() {
        simpleAccountRealm.addAccount("wmyskxz", "123456", "admin", "user");
    }

    @Test
    public void hashPassword(){ // 测试密码加密
        String password = "zhangzhangSan";
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;  // 加密次数：2
        String alogrithmName = "md5";   // 加密算法

        String encodePassword = new SimpleHash(alogrithmName, password, salt, times).toString();

        System.out.printf("原始密码是 %s \n盐是： %s\n运算次数是： %d\n运算出来的密文是：%s ",password,salt,times,encodePassword);
    }

}
