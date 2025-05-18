package com.zllUserCenter.findfriendbackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserVo implements Serializable {


    private static final long serialVersionUID = -838255695229207147L;
    /**
     *id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;


    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0-正常 -1
     */
    private Integer userStatus;

    /**
     * 电话
     */
    private String phone;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 用户角色：1-管理员, 0-普通用户
     */
    private Integer userRole;

    /**
     * 星球编号
     */
    private String planetCode;

}
