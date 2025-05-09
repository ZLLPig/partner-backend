package com.zllUserCenter.findfriendbackend.model.dto;


import com.zllUserCenter.findfriendbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class TeamQuery extends PageRequest {

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
     * 公开 - 0 私有 - 1  加密 - 2
     */
    private Integer status;

    /**
     * 描述
     */
    private String description;



}
