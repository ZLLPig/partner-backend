package com.zllUserCenter.findfriendbackend.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 队伍信息（脱敏）
 */
@Data
public class TeamUserVo implements Serializable {


    private static final long serialVersionUID = -2607792788980528889L;
    /**
     * id
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;


    /**
     * 公开 - 0 私有 - 1  加密 - 2
     */
    private Integer status;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人用户信息
     */
    UserVo createUser;

    /**
     * 已加入的用户数
     */
    private Integer hasJoinNum;

    /**
     * 用户是否已加入
     */
    private boolean hasJoin = false;

}
