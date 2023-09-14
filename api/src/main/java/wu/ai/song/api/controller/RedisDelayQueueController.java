package wu.ai.song.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import wu.ai.song.api.timeout.RedisDelayQueueEnum;
import wu.ai.song.api.timeout.RedisDelayQueueUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 延迟队列测试
 * Created by 2023年6月25日11:37:54.
 */
@RestController
public class RedisDelayQueueController {

    @Autowired
    private RedisDelayQueueUtil redisDelayQueueUtil;

    @PostMapping("/addQueue")
    public void addQueue() {
        Map<String, String> map1 = new HashMap<>(2);
        map1.put("orderId", "100");
        map1.put("remark", "订单支付超时，自动取消订单");

        Map<String, String> map2 = new HashMap<>(2);
        map2.put("orderId", "200");
        map2.put("remark", "订单超时未评价，系统默认好评");

        // 添加订单支付超时，自动取消订单延迟队列。为了测试效果，延迟10秒钟
        redisDelayQueueUtil.addDelayQueue(map1, 10, TimeUnit.SECONDS, RedisDelayQueueEnum.ORDER_PAYMENT_TIMEOUT.getCode());
        redisDelayQueueUtil.removeDelayedQueue(map1, RedisDelayQueueEnum.ORDER_PAYMENT_TIMEOUT.getCode());

        // 订单超时未评价，系统默认好评。为了测试效果，延迟20秒钟
        redisDelayQueueUtil.addDelayQueue(map2, 20, TimeUnit.SECONDS, RedisDelayQueueEnum.ORDER_TIMEOUT_NOT_EVALUATED.getCode());
    }

}
