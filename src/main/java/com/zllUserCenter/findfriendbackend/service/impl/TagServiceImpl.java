package com.zllUserCenter.findfriendbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zllUserCenter.findfriendbackend.model.domain.Tag;
import com.zllUserCenter.findfriendbackend.service.TagService;
import com.zllUserCenter.findfriendbackend.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author ZLL
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2025-04-25 21:32:02
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




