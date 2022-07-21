CREATE TABLE `players`
(
    `pid`  int(2)      NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    `age`  int(2)      NOT NULL,
    PRIMARY KEY (`pid`),
    UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
INSERT INTO `players` (`pid`, `name`, `age`)
VALUES (1, 'Samual', 25),
       (2, 'Vino', 20),
       (3, 'John', 20),
       (4, 'Andy', 22),
       (5, 'Brian', 21),
       (6, 'Dew', 24),
       (7, 'Kris', 25),
       (8, 'William', 26),
       (9, 'George', 23),
       (10, 'Peter', 19),
       (11, 'Tom', 20),
       (12, 'Andre', 20);
select pid, name, age, rank() over (order by age) as rank_num
from players;

select name, age
from (select pid, name, age, rank() over (order by age) as rank_num from players) as rank_table
where rank_num = 10;

alter table players
    add score int;
update players
set score=98
where pid = 1;
update players
set score=96
where pid = 2;
update players
set score=92
where pid = 3;
update players
set score=96
where pid = 4;
update players
set score=97
where pid = 5;
update players
set score=92
where pid = 6;
update players
set score=88
where pid = 7;
update players
set score=89
where pid = 8;
update players
set score=88
where pid = 9;
update players
set score=88
where pid = 10;
update players
set score=92
where pid = 11;
update players
set score=91
where pid = 12;
select *
from players;
explain
select name, age, score, rank() over (partition by age order by score desc) as rank_num
from players;
select *
from (select name, age, score, rank() over (partition by age order by score desc) as rank_num
      from players) temp
where temp.rank_num = 1;