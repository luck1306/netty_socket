package com.example.netty_socket.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.netty_socket.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
           socketService.saveMessage(client, data);
//           client.getNamespace().getBroadcastOperations().sendEvent("get message", data.getMessage());
//           "client.getNamespace().getBroadCastOperations..." send all user data include me
        });
    }

    private ConnectListener onConnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            log.info(String.valueOf(params.get("room")));
            log.info(String.valueOf(params.get("userName")));
            String room = String.valueOf(params.get("room"));
            String userName = String.valueOf(params.get("userName"));
            client.joinRoom(room);
            socketService.saveInfoMessage(client, String.format("welcome %s", userName),room);
            log.info("Socket Id[{}] Connected room - [{}] user_name - [{}]]"
                    , client.getSessionId().toString(), room, userName);
        };
    }

    private DisconnectListener onDisconnected() {
        return (client) -> {
            var params = client.getHandshakeData().getUrlParams();
            String room = params.get("room").stream().collect(Collectors.joining());
            socketService.saveInfoMessage(client, String.format("good bye, %s"), room);
            log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
        };
    }
}
