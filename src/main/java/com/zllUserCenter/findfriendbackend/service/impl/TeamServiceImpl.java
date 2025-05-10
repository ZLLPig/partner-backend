package com.zllUserCenter.findfriendbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zllUserCenter.findfriendbackend.exception.BusinessException;
import com.zllUserCenter.findfriendbackend.exception.ErrorCode;
import com.zllUserCenter.findfriendbackend.model.domain.Team;
import com.zllUserCenter.findfriendbackend.model.domain.User;
import com.zllUserCenter.findfriendbackend.model.domain.UserTeam;
import com.zllUserCenter.findfriendbackend.model.enums.TeamStatusEnum;
import com.zllUserCenter.findfriendbackend.service.TeamService;
import com.zllUserCenter.findfriendbackend.mapper.TeamMapper;
import com.zllUserCenter.findfriendbackend.service.UserService;
import com.zllUserCenter.findfriendbackend.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * @author ZLL
 * @description 针对表【team(队伍表)】的数据库操作Service实现
 * @createDate 2025-05-08 23:38:51
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

    @Resource
    private UserTeamService userTeamService;


    /**
     * 添加队伍
     *
     * @param team
     * @param loginUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    //回滚是指数据库事务执行过程中，如果发生错误或异常，系统会撤销已执行的所有操作，使数据库恢复到事务开启前的状态
    public long addTeam(Team team, User loginUser) {

        //1. 请求参数是否为空
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2. 是否登录，未登录不许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        final long userId = loginUser.getId();
        //3. 校验信息：
        //   1. 队伍人数 > 1 且 <= 20
        int teamNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (teamNum < 1 || teamNum > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不合法");
        }
        //   2. 队伍标题 <= 20
        String teamName = team.getName();
        if (StringUtils.isNotBlank(teamName) && teamName.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不合法");
        }
        //   3. 描述 <= 512
        String description = team.getDescription();
        if (StringUtils.isBlank(description) || description.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述不合法");
        }
        //   4. status是否公开(int) 不传默认为0(公开)
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getTeamEnumValue(status);
        //   5. 如果status是加密状态，一定要有密码，且密码 <= 32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > 32) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍密码不合法");
            }
        }
        //   6. 超时时间 > 当前时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "超时时间不合法");
        }
        //   7. 校验用户最多创建5个队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long teamCount = this.count(queryWrapper);
        if (teamCount >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户最多创建5个队伍");
        }
        //4. 插入队伍信息到队伍表
        team.setId(null);
        team.setUserId((int)userId);
//    save作用：将team信息插入到team数据表中
        boolean result = this.save(team);
        if(!result || team.getId() == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"创建队伍失败");
        }
        //5. 插入用户 => 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId((int)userId);
        userTeam.setTeamId(Math.toIntExact(team.getId()));
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if(!result){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"创建队伍失败");
        }
        return team.getId();
    }
}




