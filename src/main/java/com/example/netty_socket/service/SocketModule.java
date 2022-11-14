package com.example.netty_socket.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.netty_socket.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocketModule {
    private final SocketIOServer server;

    private final SocketService socketService;

    public SocketModule(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;
        server.addConnectListener(onConnected()); // if connect socket : execute
        server.addDisconnectListener(onDisconnected()); // if disconnect socket : execute
        server.addEventListener("send_message", Message.class, onChatReceived());
        // "server.addEventListener..." can handle corresponding eventName : ("send_message")
    }

    private DataListener<Message> onChatReceived() {
        return ((client, data, ackSender) -> {
           log.info(data.toString());
           socketService.sendMessage(data.getRoom(), "get_message", client, data.getMessage());
//           client.getNamespace().getBroadcastOperations().sendEvent("get message", data.getMessage());
//           "client.getNamespace().getBroadCastOperations..." send all user data include me
        });
    }

    private ConnectListener onConnected() {
        return (client -> {
            String room = client.getHandshakeData().getSingleUrlParam("room");
            client.joinRoom(room);
            log.info("Socket Id[{}] Connected to socket", client.getSessionId().toString());
        });
    }

    private DisconnectListener onDisconnected() {
        return (client) -> {
            log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
        };
    }
}
