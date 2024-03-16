package aisong.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 客户端和服务端都是通过事件来交互的 用于监听客户端websocket的事件 同时也可以往客户端发送事件(客户端自己可以监听)
 */
@Component
@Slf4j
public class MessageEventHandler {
    @Autowired
    private SocketIOServer socketIoServer;
    /**
     * 线程安全的map,用于保存和客户端的回话 如果是使用集群部署的情况下则不能这么使用, 因为客户端每次命中的服务不一定是上次命中那个 集群解决方案:使用redis的发布订阅或者消息中间件的发布订阅
     * 这样,每个服务都有listener监听着,然后可以拿到对应的客户端SocketClient
     */
    public static ConcurrentMap<String, SocketIOClient> socketIOClientMap = new ConcurrentHashMap<>();

    /**
     * 客户端连接的时候触发
     *
     * @param client 客户算
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        String mac = client.getHandshakeData().getSingleUrlParam("mac");
        // 存储SocketIOClient，用于发送消息
        socketIOClientMap.put(mac, client);
        // 通过client.sendEvent可以往客户端回发消息
        client.sendEvent("message", "onConnect back");
        log.info("客户端: {} 已连接", client.getHandshakeData().getSingleUrlParam("mac"));
    }

    /**
     * 客户端关闭连接时触发
     *
     * @param client 客户端
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String mac = client.getHandshakeData().getSingleUrlParam("mac");
        socketIOClientMap.remove(mac);
        log.info("客户端: {} 断开连接", client.getHandshakeData().getSingleUrlParam("mac"));
    }

    /**
     * 监听客户端事件messageEvent
     *
     * @param client 　客户端信息
     * @param data   　客户端发送数据
     */
    @OnEvent(value = "messageEvent")
    public void messageEvent2(SocketIOClient client, JSONObject data) {
        String time = data.getString("time");
        String clientMac = client.getHandshakeData().getSingleUrlParam("mac");
        // 广播消息
        client.sendEvent("messageEvent", StrUtil.format("{},发送了 {}", clientMac, time));
        for (SocketIOClient clientTemp : socketIOClientMap.values()) {
            String clientTempMac = clientTemp.getHandshakeData().getSingleUrlParam("mac");
            if (clientTemp.isChannelOpen() && ObjectUtil.notEqual(clientTempMac, clientMac)) {
                clientTemp.sendEvent("Broadcast", StrUtil.format("{},发送了 {}", clientMac, time));
            }
        }
    }
}
