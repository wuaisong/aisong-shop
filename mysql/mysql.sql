DROP TABLE IF EXISTS user;
CREATE TABLE USER
(
    id    BIGINT(20)  NOT NULL COMMENT '主键ID',
    NAME  VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age   INT(11)     NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (id)
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
