package com.zllUserCenter.findfriendbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页请求
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -53543624131574753L;
    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 当前第几页
     */
    private int pageNum = 1;

}
