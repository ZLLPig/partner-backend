package com.zllUserCenter.findfriendbackend.service;

import com.zllUserCenter.findfriendbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author ZLL
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-04-25 21:12:27
*/
public interface UserService extends IService<User> {



    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);


    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);


    /**
     * 根据标签搜索用户
     * @param tagNameList
     * @return
     */
    List<User> searchUser(List<String> tagNameList);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);


    /**
     * 根据标签搜索用户
     *
     * @param tagNameList
     * @return
     */
    List<User> searchUserByTags(List<String> tagNameList);


}
