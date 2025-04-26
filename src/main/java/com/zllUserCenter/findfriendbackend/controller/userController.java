package com.zllUserCenter.findfriendbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zllUserCenter.findfriendbackend.common.BaseResponse;
import com.zllUserCenter.findfriendbackend.common.ResultUtils;
import com.zllUserCenter.findfriendbackend.exception.BusinessException;
import com.zllUserCenter.findfriendbackend.exception.ErrorCode;
import com.zllUserCenter.findfriendbackend.model.domain.User;
import com.zllUserCenter.findfriendbackend.model.domain.request.UserLoginRequest;
import com.zllUserCenter.findfriendbackend.model.domain.request.UserRegisterRequest;
import com.zllUserCenter.findfriendbackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.zllUserCenter.findfriendbackend.constant.UserConstant.ADMIN_ROLE;
import static com.zllUserCenter.findfriendbackend.constant.UserConstant.USER_LOGIN_STATUS;


@RestController
@RequestMapping("/user")
public class userController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) throws BusinessException {
        if(userRegisterRequest == null){
           throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return null;
        }
        long result = userService.userRegister(userAccount,userPassword,checkPassword);
        return ResultUtils.success(result);
    }


    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){

        if(userLoginRequest == null){
            //输入参数校验，属于预期内的常规错误，不需要记录完整堆栈
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }

        User user = userService.userLogin(userAccount,userPassword,request);
        return ResultUtils.success(user);

    }


    /**
     * 根据名称查询用户
     *
     * @param username
     * @param request
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String username, HttpServletRequest request){
        //如果没有权限
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"缺少管理员权限");
        }
        //查询数据表
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
       // WHERE username LIKE '%输入的值%'
            queryWrapper.like("username",username);
        }
        //设置脱敏，返回给前端的时候不带上密码
        //返回符合条件的用户列表
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user ->
        userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 根据标签搜索用户
     * @param tagNameList
     * @return
     */
    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUserByTags(@RequestParam(required = false) List<String> tagNameList){
        if(CollectionUtils.isEmpty(tagNameList)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        List<User> userList = userService.searchUserByTags(tagNameList);
        return ResultUtils.success(userList);
    }


    /**
     * 用户删除
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request){
        //没有权限不能进行删除
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH, "缺少管理员权限");
        }
        //没有Id不能删除
        if(id <= 0){
           throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }


    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> currentUser(HttpServletRequest request){
        //得到当前登录态
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATUS);
        if(currentUser == null){
           throw new BusinessException(ErrorCode.NOT_LOGIN, "未登录");
        }

        //从当前登录用户中获取登录Id
        long userId = currentUser.getId();
        //根据用户信息从数据库查询完整的用户信息
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        //对查询到的用户进行脱敏处理，返回安全信息
        return ResultUtils.success(safetyUser);
    }

    /**
     * 用户权限
     * @param request
     * @return
     */
    public boolean isAdmin(HttpServletRequest request){
        //身份鉴权，仅管理员可查询
        //拿到用户信息
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATUS);
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }


    /**
     * 用户注销
     * @param request
     * @return
     */
    public BaseResponse<Integer> userLogOut(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
       if(request == null){
           throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
       }
       int result = userService.userLogout(request);
       return ResultUtils.success(result);
    }



}