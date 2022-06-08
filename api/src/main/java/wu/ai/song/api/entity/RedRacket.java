package wu.ai.song.api.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wuyaming 2022/6/8.
 */
@Getter
@Setter
@ToString
public class RedRacket {
    /*
CREATE TABLE `red_racket` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
   `red_packet_id` bigint(20) NOT NULL COMMENT '红包唯一ID',
   `total_amount` int(11) NOT NULL COMMENT '红包金额单位分',
   `total_packet` int(11) NOT NULL COMMENT '红包个数',
   `type` int(11) NOT NULL COMMENT '红包类型',
   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
   `version` int(11) NOT NULL COMMENT '版本号',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='红包信息表'
     */
}
