package com.zllUserCenter.findfriendbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zllUserCenter.findfriendbackend.mapper")

public class FindFriendBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindFriendBackendApplication.class, args);
    }

}
