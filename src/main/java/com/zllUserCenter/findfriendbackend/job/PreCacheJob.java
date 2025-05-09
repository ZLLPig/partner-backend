package com.zllUserCenter.findfriendbackend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zllUserCenter.findfriendbackend.model.domain.User;
import com.zllUserCenter.findfriendbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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

    @Resource
    private RedissonClient redissonClient;

    //重点用户
    private List<Long> mainUserList = Arrays.asList(1L);


    //每天执行，预热推荐用户
    @Scheduled(cron = "0 0 3 * * *")
    public void doCache() {
        RLock lock = redissonClient.getLock("friend:precachejob:doCache:lock");
        try{
            if(lock.tryLock(0,-1,TimeUnit.MILLISECONDS)){
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }

    }
}
