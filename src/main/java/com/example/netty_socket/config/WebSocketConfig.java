package com.example.netty_socket.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketConfig {


    @Value("${socket-server.port}")
    private Integer port;

    @Value("${socket-server.host}")
    private String host;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
//        SocketConfig socketConfig = new SocketConfig();
//        socketConfig.setReuseAddress(true);
        config.setHostname(host);
        config.setPort(port);
//        config.setOrigin("*");
//        config.setSocketConfig(socketConfig);
        return new SocketIOServer(config);
    }
}
