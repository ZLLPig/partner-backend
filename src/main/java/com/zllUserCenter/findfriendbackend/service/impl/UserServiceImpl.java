package com.zllUserCenter.findfriendbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zllUserCenter.findfriendbackend.exception.BusinessException;
import com.zllUserCenter.findfriendbackend.exception.ErrorCode;
import com.zllUserCenter.findfriendbackend.mapper.TagMapper;
import com.zllUserCenter.findfriendbackend.model.domain.User;
import com.zllUserCenter.findfriendbackend.service.UserService;
import com.zllUserCenter.findfriendbackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.zllUserCenter.findfriendbackend.constant.UserConstant.ADMIN_ROLE;
import static com.zllUserCenter.findfriendbackend.constant.UserConstant.USER_LOGIN_STATUS;

/**
* @author ZLL
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-04-25 21:12:27
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{


    @Resource
    private final UserMapper userMapper;
    @Resource
    private TagMapper tagMapper;


    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    /**
     * 用户注册
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //校验所有参数不能为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        //校验用户长度不能小于四
        if (userAccount.length() < 4) {
            return -1;
        }
        //校验密码长度不能小于八
        if (userPassword.length() < 8) {
            return -1;
        }
        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        //校验两次密码是否一致
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        //用户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }
        //密码加密
        final String SALT = "Zll";
        String encryptPassword = DigestUtils.md5DigestAsHex((userPassword + SALT).getBytes());
        //向用户数据库插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        //调用 MyBatis-Plus 提供的 save() 方法，将 user 对象插入数据库
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        //如果插入成功，返回新用户的数据库ID
        return user.getId();
    }


    /**
     * 用户登录
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //校验所有参数不能为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        //校验用户长度不能小于四
        if (userAccount.length() < 4) {
            return null;
        }
        //校验密码长度不能小于八
        if (userPassword.length() < 8) {
            return null;
        }
        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        //加密
        final String SALT = "Zll";
        String encryptPassword = DigestUtils.md5DigestAsHex((userPassword + SALT).getBytes());
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed,userPassword cannot match userAccount");
            return null;
        }

        // 用户脱敏
        User safetyUser = getSafetyUser(user);

        //记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATUS, safetyUser);

        return null;
    }


    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        safetyUser.setTags(originUser.getTags());

        return safetyUser;
    }


    /**
     * 根据名称搜索用户
     *
     * @param userName
     * @return
     */
    @Override
    public List<User> searchUser(List<String> userName, HttpServletRequest request) {
        if (CollectionUtils.isEmpty(userName)) {
           throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        return new ArrayList<>();
    }

    /**
     * 标签搜索用户
     *
     * @param tagNameList
     * @return
     */
    @Override
    public List<User> searchUserByTags(List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        //1.查询所有用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        //2.在内存中判断是否包含要求的标签
        return userList.stream().filter(user -> {
            String tagStr = user.getTags();
            Set<String> tempTagNameSet = gson.fromJson(tagStr, new TypeToken<Set<String>>() {}.getType());
            tempTagNameSet = Optional.ofNullable(tempTagNameSet).orElse(new HashSet<>());
            for (String tagName : tagNameList) {
                if (!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());
    }


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATUS);
        return 1;
    }

    @Override
    public Integer updateUser(User user,User LoginUser) {
        long userId = user.getId();
        if(userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        //如果是管理员可以更新任何用户
        //如果不是管理员 仅能修改自己的数据
        if(!isAdmin(LoginUser) || userId != LoginUser.getId()) {
            throw new BusinessException(ErrorCode.NO_AUTH,"缺少权限");
        }
            User oldUser = userMapper.selectById(userId);
            if(oldUser == null){
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"请求数据不存在");
            }
            return userMapper.updateById(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        if(request == null){
            return null;
        }
        Object userObj =  request.getSession().getAttribute(USER_LOGIN_STATUS);
        if(userObj == null){
            throw new BusinessException(ErrorCode.NO_AUTH,"用户未登录");
        }
        return (User) userObj;
    }

    /**
     * 用户权限
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request){
        //身份鉴权，仅管理员可查询
        //拿到用户信息
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATUS);
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

    /**
     * 用户权限
     * @param userLogin
     * @return
     */
    @Override
    public boolean isAdmin(User userLogin){
        return userLogin != null && userLogin.getUserRole() == ADMIN_ROLE;
    }



}




