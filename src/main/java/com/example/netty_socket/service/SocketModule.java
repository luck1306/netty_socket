package com.example.netty_socket.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.netty_socket.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocketModule {
    private final SocketIOServer server;

    public SocketModule(SocketIOServer server) {
        this.server= server;
        server.addConnectListener(onConnected()); // if connect socket : execute
        server.addDisconnectListener(onDisconnected()); // if disconnect socket : execute
        server.addEventListener("send_message", Message.class, onChatReceived());
        // "server.addEventListener..." can handle corresponding eventName : ("send_message")
    }

    private DataListener<Message> onChatReceived() {
        return ((client, data, ackSender) -> {
           log.info(data.toString());
           client.getNamespace().getBroadcastOperations().sendEvent("get message", data.getMessage());
           // "client.getNamespace()...." send all user data include me
        });
    }

    private ConnectListener onConnected() {
        return (client -> {
            log.info("Socket Id[{}] Connected to socket", client.getSessionId().toString());
        });
    }

    private DisconnectListener onDisconnected() {
        return (client) -> {
            log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
        };
    }
}
