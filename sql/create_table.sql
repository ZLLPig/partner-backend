use user;

--  队伍表
create table team
(
    id          bigint auto_increment primary key,
    name        varchar(256)                       not null comment '队伍名称',
    userId      int comment '用户id',
    maxNum      int      default 1                 not null comment '最大人数',
    expireTime  datetime                           null comment '过期时间',
    password    varchar(512)                       null comment '密码',
    status      int      default 0                 null comment '公开 - 0 私有 - 1  加密 - 2',
    description varchar(1024)                      null comment '描述',
    createTime  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '队伍表';

-- 用户_队伍关系表
create table user_team
(
    id         bigint auto_increment primary key,
    userId     int comment '用户id',
    teamId     int comment '队伍id',
    joinTime   datetime                           null comment '加入时间',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '用户_队伍表';