package com.zllUserCenter.findfriendbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

/**
 * 队伍表
 * @TableName team
 */
@TableName(value ="team")
@Data
public class Team {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
}