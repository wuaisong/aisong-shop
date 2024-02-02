package aisong.utils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author DaHuaJia
 * @Describe 环保设备对接程序启动类，Netty服务器端启动类，接收设备上传的数据。
 * @Date 2022-10-09 11:08:32
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EPServer implements ApplicationRunner {
    private final EPServerHandler epServerHandler;
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(2);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(4);

    public void runEPServer() {
        InetSocketAddress endpoint = new InetSocketAddress("127.0.0.1", 9999);
        log.info("===== EP Netty Server start =====");
        try {
            ServerBootstrap b = new ServerBootstrap();
            // 创建事件循环组
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ChannelPipeline pipeline = ch.pipeline();
                    // 解决拆包问题
                    ByteBuf delimiter = Unpooled.copiedBuffer("\r\n", CharsetUtil.UTF_8);
                    pipeline.addLast(new DelimiterBasedFrameDecoder(2048, delimiter));
                    pipeline.addLast("serverDecoder", new StringDecoder(CharsetUtil.UTF_8));
                    pipeline.addLast("serverEncoder", new StringEncoder(CharsetUtil.UTF_8));
                    // netty提供了空闲状态监测处理器 0表示禁用事件
                    pipeline.addLast(new IdleStateHandler(65, 0, 0, TimeUnit.MINUTES));
                    pipeline.addLast(epServerHandler);
                }
            });
            log.info("环保 Netty Server PORT = 9999");
            b.bind(9999).sync();
        } catch (Exception e) {
            log.error("error: {}", e.getMessage());
            shutdown();
        }
    }

    /**
     * 关闭服务
     */
    public static void shutdown() {
        // 优雅关闭
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    @Override
    public void run(ApplicationArguments args) {
        // 启动环保监测Netty服务端
        runEPServer();
        new Thread(() -> {
            try (Socket socket = new Socket("localhost", 9999); OutputStream os = socket.getOutputStream(); PrintWriter pw = new PrintWriter(os); InputStream is = socket.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
                pw.write("##0190ST=31;CN=2061;PW=123456;MN=7568770259402;Flag=0;CP=&&DataTime=20221008100000;B02-Min=1.6960,B02-Avg=3.0586,B02-Max=3.7704,B02-Cou=11010.8437;S01-Min=17.7469,S01-Avg=19.4636,S01-Max=19.6944&&7BC0");
                // 注意：需要手动发送换行符
                pw.println();
                pw.flush();
                socket.shutdownOutput();
                // 从服务器接收的信息
                String info;
                while ((info = br.readLine()) != null) {
                    log.info("return: {}", info);
                }
            } catch (Exception e) {
                log.error("error: {}", e.getMessage());
            }
        }).start();
    }

}
