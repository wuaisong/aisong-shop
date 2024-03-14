package aisong;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class NettyApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(NettyApplication.class, args);
    }

    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void run(String... strings) {
        socketIOServer.start();
    }
}
