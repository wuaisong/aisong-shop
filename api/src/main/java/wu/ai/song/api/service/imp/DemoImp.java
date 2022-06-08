package wu.ai.song.api.service.imp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wu.ai.song.api.entity.RedPacketRecord;
import wu.ai.song.api.redis.RedisUtil;
import wu.ai.song.api.redis.RedissLockUtil;
import wu.ai.song.api.service.Demo;
import wu.ai.song.api.utils.Result;

import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author wuyaming 2022/6/8.
 */
@Service
@AllArgsConstructor
@Slf4j
public class DemoImp implements Demo {

    private final RedisUtil redisUtil;

    private final RedissonClient redissonClient;

    @Override
    @Transactional
    public Result startTwoSeckil(long redPacketId, int userId) {
        Integer money = 0;
        boolean res = false;
        try {
            /**
             * 获取锁 保证红包数量和计算红包金额的原子性操作
             */
            res = RedissLockUtil.tryLock(redPacketId + "", TimeUnit.SECONDS, 3, 10);
            if (res) {
                long restPeople = redisUtil.incrBy(redPacketId + "-num", -1);
                if (restPeople >= 0) {
                    /**
                     * 如果是最后一人
                     */
                    if (restPeople == 0) {
                        money = Integer.parseInt(redisUtil.get(redPacketId + "-money").toString());
                    } else {
                        Integer restMoney = Integer.parseInt(redisUtil.get(redPacketId + "-money").toString());
                        Random random = new Random();
                        //随机范围：[1,剩余人均金额的两倍]
                        money = random.nextInt((int) (restMoney / (restPeople + 1) * 2 - 1)) + 1;
                    }
                    redisUtil.incrBy(redPacketId + "-money", -money);
                    /**
                     * 异步入库
                     */
                    RedPacketRecord record = new RedPacketRecord();
                    record.setMoney(money);
                    record.setRedPacketId(redPacketId);
                    record.setUid(userId);
                    record.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    saveRecord(record);
                    /**
                     * 异步入账
                     */
                } else {
                    return Result.error("手慢了，红包派完了");
                }
            } else {
                /**
                 * 获取锁失败相当于抢红包失败
                 */
                return Result.error("手慢了，红包派完了");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res) {//释放锁
                RedissLockUtil.unlock(redPacketId + "");
            }
        }
        return Result.success(money.toString());
    }

    private void saveRecord(RedPacketRecord record) {
        log.info(record.toString());
    }
}
