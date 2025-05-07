package com.zllUserCenter.findfriendbackend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zllUserCenter.findfriendbackend.mapper.UserMapper;
import com.zllUserCenter.findfriendbackend.model.domain.User;
import com.zllUserCenter.findfriendbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    //重点用户
    private List<Long> mainUserList = Arrays.asList(1L);

    @Scheduled(cron = "0 0 3 * * *")
    public void doCache() {
        for (Long userId : mainUserList) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
            String redisKey = String.format("user:recommend:%s", userId);
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            //写缓存
            try {
                //设置缓存，key为redisKey，value为userPage，过期时间为30000毫秒
                valueOperations.set(redisKey, userPage, 30000, TimeUnit.MICROSECONDS);
            } catch (Exception e) {
                //记录错误日志
                log.error("redis set error", e);
            }
        }
    }
}
