package com.zllUserCenter.findfriendbackend.once;


import cn.hutool.core.date.StopWatch;
import com.zllUserCenter.findfriendbackend.mapper.UserMapper;
import com.zllUserCenter.findfriendbackend.model.domain.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InsertUsers {

    @Resource
    private UserMapper userMapper;

//    @Scheduled(initialDelay = 5000 ,fixedDelay = Long.MAX_VALUE)
    public void doInsertUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 100000;
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("假zll");
            user.setUserAccount("fake zll");
            user.setAvatarUrl("https://deepseek-user-avatar.obs.cn-east-3.myhuaweicloud.com/a/LNebtO7JPyzK39QGXRnT_pcF");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setEmail("123@qq.com");
            user.setUserStatus(0);
            user.setPhone("");
            user.setTags("[大美女],[富婆]");
            user.setUserRole(0);
            user.setPlanetCode("777");
//            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

}
