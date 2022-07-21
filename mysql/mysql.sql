DROP TABLE IF EXISTS user;
create table user
(
    id           bigint                             not null comment '主键ID'
        primary key,
    name         varchar(300)                       null comment '姓名',
    age          int                                null comment '年龄',
    email        varchar(500)                       null comment '邮箱',
    gmt_create   datetime default CURRENT_TIMESTAMP null,
    gmt_update   datetime default CURRENT_TIMESTAMP null,
    version      int      default 0                 null,
    deleted      int      default 0                 null,
    gmt_modified datetime default CURRENT_TIMESTAMP null
);
DELETE
FROM user;
INSERT INTO user (id, NAME, age, email)
VALUES (1, 'Jone', 18, 'test1@baomidou.com'),
       (2, 'Jack', 20, 'test2@baomidou.com'),
       (3, 'Tom', 28, 'test3@baomidou.com'),
       (4, 'Sandy', 21, 'test4@baomidou.com'),
       (5, 'Billie', 24, 'test5@baomidou.com');
-- 真实开发中，version（乐观锁）、deleted（逻辑删除）、gmt_create、gmt_modified


-- 已有数据表添加自增策略

alter table user
    change id id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID';


-- 已有数据表添加新字段
alter table user
    add gmt_create date; -- 开始时间
alter table user
    add gmt_modified date;
-- 结束时间


# 乐观锁：1、先查询，获得版本号 version = 1
-- A
update user
set name    = 'kuangshen',
    version = version + 1
where id = 2
  and version = 1;
-- B 线程抢先完成，这个时候 version = 2，会导致 A 修改失败！
update user
set name    = 'kuangshen',
    version = version + 1
where id = 2
  and version = 1;

select count(1) from user;
