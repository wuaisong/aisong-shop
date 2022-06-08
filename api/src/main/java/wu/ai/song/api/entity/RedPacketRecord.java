package wu.ai.song.api.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * @author wuyaming 2022/6/8.
 */
@Getter
@Setter
@ToString
public class RedPacketRecord {
    private Integer money;
    private long redPacketId;
    private long uid;
    private Timestamp createTime;

    /*
CREATE TABLE `red_packet_record` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
   `amount` int(11) NOT NULL COMMENT '抢到红包的金额',
   `red_packet_id` bigint(20) NOT NULL COMMENT '红包ID',
   `uid` int(11) NOT NULL COMMENT '抢到红包用户的用户标识',
   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='抢红包记录表'
     */
}
