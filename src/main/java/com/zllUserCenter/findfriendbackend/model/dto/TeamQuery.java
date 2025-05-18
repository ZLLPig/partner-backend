package com.zllUserCenter.findfriendbackend.model.dto;


import com.zllUserCenter.findfriendbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class TeamQuery extends PageRequest {

    private Long id;

    /**
     * id 列表
     */
    private List<Long> idList;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 搜索内容（同时对名称和描述搜索）
     */
    private String searchText;

    /**
     * 用户id
     */
    private Long userId;

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
