package com.zllUserCenter.findfriendbackend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zllUserCenter.findfriendbackend.exception.BusinessException;
import com.zllUserCenter.findfriendbackend.exception.ErrorCode;
import com.zllUserCenter.findfriendbackend.manage.model.StpKit;
import com.zllUserCenter.findfriendbackend.mapper.TagMapper;
import com.zllUserCenter.findfriendbackend.model.domain.User;
import com.zllUserCenter.findfriendbackend.service.UserService;
import com.zllUserCenter.findfriendbackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.zllUserCenter.findfriendbackend.constant.UserConstant.*;

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
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if(StrUtil.hasBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userPassword.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
        if(userPassword.length()<8 || checkPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入的密码不一致");
        }
        // 2. 检查是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        Long count = this.baseMapper.selectCount(queryWrapper);
        if(count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }
        // 3. 加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }


    @Override
    public User userLogin(@RequestBody String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATUS, user);
        StpKit.SPACE.login(user.getId());
        StpKit.SPACE.getSession().set(USER_LOGIN_STATUS, user);
        return user;
    }

    /**
     * 获取加密后的密码
     * @param userPassword
     * @return
     */
    @Override
    public String getEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        final String SALT = "zll";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 判断是否已经登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User currentUser = (User) userObj;
        if(currentUser == null || currentUser.getId() == 0){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        // 从数据库中查询
        Long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if(currentUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return currentUser;
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

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
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




