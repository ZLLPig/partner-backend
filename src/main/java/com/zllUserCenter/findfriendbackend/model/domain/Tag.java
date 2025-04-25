package com.zllUserCenter.findfriendbackend.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName tag
 */
@TableName(value ="tag")
@Data
public class Tag {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String tagName;

    /**
     * 
     */
    private Long uerId;

    /**
     * 
     */
    private Long parentId;

    /**
     * 
     */
    private Integer isParent;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private Integer isDelete;
}