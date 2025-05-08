package com.zllUserCenter.findfriendbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zllUserCenter.findfriendbackend.model.domain.Team;
import com.zllUserCenter.findfriendbackend.service.TeamService;
import com.zllUserCenter.findfriendbackend.mapper.TeamMapper;
import org.springframework.stereotype.Service;

/**
* @author ZLL
* @description 针对表【team(队伍表)】的数据库操作Service实现
* @createDate 2025-05-08 23:38:51
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

}




