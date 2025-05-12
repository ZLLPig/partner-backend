package com.zllUserCenter.findfriendbackend.model.request;


import lombok.Data;

import java.util.Date;

@Data
public class TeamJoinRequest {

    /**
     * 队伍id
     */
    private Long teamId;


    /**
     * 密码
     */
    private String password;


}
