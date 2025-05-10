package com.zllUserCenter.findfriendbackend.model.request;


import lombok.Data;

import java.util.Date;

@Data
public class TeamAddRequest {

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 密码
     */
    private String password;

    /**
     * 公开 - 0 私有 - 1  加密 - 2
     */
    private Integer status;

    /**
     * 描述
     */
    private String description;

}
