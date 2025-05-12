package com.zllUserCenter.findfriendbackend.service;

import com.zllUserCenter.findfriendbackend.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zllUserCenter.findfriendbackend.model.domain.User;
import com.zllUserCenter.findfriendbackend.model.dto.TeamQuery;
import com.zllUserCenter.findfriendbackend.model.request.TeamJoinRequest;
import com.zllUserCenter.findfriendbackend.model.request.TeamUpdateRequest;
import com.zllUserCenter.findfriendbackend.model.vo.TeamUserVo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;

/**
* @author ZLL
* @description 针对表【team(队伍表)】的数据库操作Service
* @createDate 2025-05-08 23:38:51
*/
public interface TeamService extends IService<Team> {


    /**
     * 添加队伍
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);

    /**
     * 搜索队伍
     * @param teamQuery
     * @return
     */
    List<TeamUserVo> listTeam(TeamQuery teamQuery,boolean isAdmin);

    /**
     * 更新队伍信息
      * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest,User loginUser);


    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);
}
