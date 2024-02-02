package aisong.utils;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @Description 环保设备数据处理类，处理设备的上传数据
 **/
@Slf4j
@Component
@ChannelHandler.Sharable
public class EPServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 定义一个HashMap，用于保存所有的channel和设备ID的对应关系。
     */
    private static final Map<ChannelId, String> deviceInfo = new HashMap<>(64);

    /**
     * 消息读取
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("=============" + (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + "=============" + deviceInfo.size());
        log.info("收到" + ctx.channel().id() + "设备消息 ===> " + msg);

        // 解析物联网设备发来的数据
        JSONObject data = HJ212MsgUtils.dealMsg1((String) msg);
        // 做自己的业务逻辑，分发数据，分析数据，持久化数据等。
        ctx.writeAndFlush("test");
        log.info("设备消息解析JSON结果：" + data.toJSONString());
    }

    /***
     * 超时关闭socket 连接
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读超时";
                    break;
                case WRITER_IDLE:
                    eventType = "写超时";
                    break;
                case ALL_IDLE:
                    eventType = "读写超时";
                    break;
                default:
                    eventType = "设备超时";
            }
            log.warn(ctx.channel().id() + " : " + eventType + "---> 关闭该设备");
            ctx.channel().close();
        }
    }

    /**
     * 异常处理, 出现异常关闭channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("========= 链接出现异常！exceptionCaught.");
        ctx.fireExceptionCaught(cause);
        // 将通道从deviceInfo中删除
        deviceInfo.remove(ctx.channel().id());
        // 关闭通道链接，节省资源
        ctx.channel().close();
    }

    /**
     * 每加入一个新的链接，保存该通道并写入上线日志。该方法在channelRead方法之前执行
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        // 获取设备ID
        String deviceId = "abc";
        // 将该链接保存到deviceInfo
        deviceInfo.put(ctx.channel().id(), deviceId);
        log.warn("========= " + ctx.channel().id() + "设备加入链接。");
    }

    /**
     * 每去除一个新的链接，去除该通道并写入下线日志
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        // 将通道从deviceInfo中删除
        deviceInfo.remove(ctx.channel().id());
        log.warn("========= " + ctx.channel().id() + "设备断开链接。");
    }

}

