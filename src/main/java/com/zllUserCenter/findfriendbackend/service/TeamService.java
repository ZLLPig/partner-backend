package com.zllUserCenter.findfriendbackend.service;

import com.zllUserCenter.findfriendbackend.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zllUserCenter.findfriendbackend.model.domain.User;

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

}
