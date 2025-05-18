package com.zllUserCenter.findfriendbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zllUserCenter.findfriendbackend.common.BaseResponse;
import com.zllUserCenter.findfriendbackend.common.ResultUtils;
import com.zllUserCenter.findfriendbackend.exception.BusinessException;
import com.zllUserCenter.findfriendbackend.exception.ErrorCode;
import com.zllUserCenter.findfriendbackend.model.domain.Team;
import com.zllUserCenter.findfriendbackend.model.domain.User;
import com.zllUserCenter.findfriendbackend.model.domain.UserTeam;
import com.zllUserCenter.findfriendbackend.model.dto.TeamQuery;
import com.zllUserCenter.findfriendbackend.model.request.*;
import com.zllUserCenter.findfriendbackend.model.vo.TeamUserVo;
import com.zllUserCenter.findfriendbackend.service.TeamService;
import com.zllUserCenter.findfriendbackend.service.UserService;
import com.zllUserCenter.findfriendbackend.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import javax.servlet.http.HttpServletRequest;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/team")  //统一前缀
@CrossOrigin(origins = {"http://localhost:5173"})
@Slf4j
public class TeamController {


    private final TeamService teamService;
    private final UserService userService;
    private final ResourceUrlProvider resourceUrlProvider;
    private final UserTeamService userTeamService;

    public TeamController(TeamService teamService, UserService userService, ResourceUrlProvider resourceUrlProvider, UserTeamService userTeamService) {
        this.teamService = teamService;
        this.userService = userService;
        this.resourceUrlProvider = resourceUrlProvider;
        this.userTeamService = userTeamService;
    }

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest,HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.updateTeam(teamUpdateRequest,loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeam(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "查询失败");
        }
        return ResultUtils.success(team);
    }

    @GetMapping("/list")
    public BaseResponse<List<TeamUserVo>> listTeam(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean Admin = userService.isAdmin(request);
        List<TeamUserVo> teamList = teamService.listTeam(teamQuery, Admin);
        return ResultUtils.success(teamList);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamsByPage(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery, team);
        Page<Team> page = new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> resultPage = teamService.page(page, queryWrapper);
        return ResultUtils.success(resultPage);
    }

    @PostMapping("/join")
     public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest,HttpServletRequest request){
        if(teamJoinRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.joinTeam(teamJoinRequest,loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request){
        if(teamQuitRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.quitTeam(teamQuitRequest,loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody TeamDeleteRequest teamDeleteRequest, HttpServletRequest request) {
        if (teamDeleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.deleteTeam(teamDeleteRequest,loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 获取我创建的队伍
     * @param teamQuery
     * @param request
     * @return
     */
    @GetMapping("/list/my")
    public BaseResponse<List<TeamUserVo>> listMyTeam(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        teamQuery.setUserId(loginUser.getId());
        List<TeamUserVo> teamList = teamService.listTeam(teamQuery, true);
        return ResultUtils.success(teamList);
    }

    /**
     * 获取我加入的队伍
     * @param teamQuery
     * @param request
     * @return
     */
    @GetMapping("/list/myJoin")
    public BaseResponse<List<TeamUserVo>> listMyJoinTeam(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
        Map<Long, List<UserTeam>> listMap = userTeamList.stream()
                .collect(Collectors.groupingBy(UserTeam::getTeamId));
        List<Long> idList = new ArrayList<>(listMap.keySet());
        teamQuery.setIdList(idList);
        List<TeamUserVo> teamList = teamService.listTeam(teamQuery, true);
        return ResultUtils.success(teamList);
    }


}
