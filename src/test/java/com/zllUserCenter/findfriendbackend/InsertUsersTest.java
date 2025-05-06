package com.zllUserCenter.findfriendbackend;


import cn.hutool.core.date.StopWatch;
import com.zllUserCenter.findfriendbackend.model.domain.User;
import com.zllUserCenter.findfriendbackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
public class InsertUsersTest {

    @Resource
    private UserService userService;

    /**
     * 批量插入用户
     */
    @Test
    public void doInsertUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 1000;
        int batchSize = 500;
        int j = 0;
        List<CompletableFuture> futureList = new ArrayList<>();
        //分10组
        for (int i = 0; i < 10; i++) {
            List<User> userList = new ArrayList<>();
            while(true){
                j++;
                User user = new User();
                user.setUsername("假zll");
                user.setUserAccount("fake zll");
                user.setAvatarUrl("https://deepseek-user-avatar.obs.cn-east-3.myhuaweicloud.com/a/LNebtO7JPyzK39QGXRnT_pcF");
                user.setGender(0);
                user.setUserPassword("12345678");
                user.setEmail("123@qq.com");
                user.setUserStatus(0);
                user.setPhone("");
                user.setTags("[]");
                user.setUserRole(0);
                user.setPlanetCode("777");
                userList.add(user);
                if(j % 10000 == 0){
                    break;
                }
            }
           // 异步执行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                    userService.saveBatch(userList,10));
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

}
