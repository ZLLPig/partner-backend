package com.zllUserCenter.findfriendbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zllUserCenter.findfriendbackend.common.BaseResponse;
import com.zllUserCenter.findfriendbackend.common.ResultUtils;
import com.zllUserCenter.findfriendbackend.exception.BusinessException;
import com.zllUserCenter.findfriendbackend.exception.ErrorCode;
import com.zllUserCenter.findfriendbackend.model.domain.Team;
import com.zllUserCenter.findfriendbackend.model.dto.TeamQuery;
import com.zllUserCenter.findfriendbackend.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")  //统一前缀
@CrossOrigin( origins = {"http://localhost:5173"})
@Slf4j
public class TeamController {


    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody Team team){
        if(team == null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean save = teamService.save(team);
        if(!save){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"插入失败");
        }
        return ResultUtils.success(team.getId());
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody long id){
        if(id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.removeById(id);
        if(!result){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"删除失败");
        }
      return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody Team team){
        if(team == null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.updateById(team);
        if(!result){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"更新失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeam(long id){
        if(id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if(team == null){
            throw  new BusinessException(ErrorCode.NULL_ERROR,"查询失败");
        }
        return ResultUtils.success(team);
    }

    @GetMapping("/list")
    public BaseResponse<List<Team>> listTeam(TeamQuery teamQuery){
      if(teamQuery == null){
          throw new BusinessException(ErrorCode.PARAMS_ERROR);
      }
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery,team);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        List<Team> list = teamService.list(queryWrapper);
        return ResultUtils.success(list);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamsByPage(TeamQuery teamQuery){
        if(teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery,team);
        Page<Team> page = new Page<>(teamQuery.getPageNum(),teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> resultPage = teamService.page(page, queryWrapper);
        return ResultUtils.success(resultPage);
    }




}
