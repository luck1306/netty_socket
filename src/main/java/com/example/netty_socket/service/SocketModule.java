package com.example.netty_socket.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.example.netty_socket.entity.Message;
import com.example.netty_socket.entity.MessageType;
import com.example.netty_socket.entity.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class SocketModule {
    private final SocketIOServer server;

    private final SocketService socketService;

    private final MessageRepository repository;

    public SocketModule(SocketIOServer server, SocketService socketService, MessageRepository repository) {
        this.server = server;
        this.socketService = socketService;
        this.repository = repository;
        // "server.addEventListener..." can handle corresponding eventName : ("send_message")
    }

    @OnEvent(value = "send_message")
    private void onChatReceived(SocketIOClient client, Message data, AckRequest ack) {
//            log.info(data.getUserName() + " : " + data.getContent());
        repository.save(data);
            socketService.sendMessage(data.getRoom(), "read_message", client, data);
//            ack.sendAckData(data); // use for ack data to server
//           client.getNamespace().getBroadcastOperations().sendEvent("get message", data.getMessage());
//           "client.getNamespace().getBroadCastOperations..." send all user data include me
        }


    @OnConnect
    private void onConnected(SocketIOClient client) {
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        String room = String.valueOf(params.get("room").toArray()[0]);
        String userName = String.valueOf(params.get("userName").toArray()[0]);
        client.joinRoom(room);
        Message message = Message.builder()
                .content(String.format("welcome %s", userName))
                .messageType(MessageType.SERVER)
                .room(room)
                .userName(userName)
                .build();
        socketService.sendMessage(room, "read_message", client, message);
//        socketService.saveInfoMessage(client, String.format("welcome %s", userName),room);
        log.info("Socket Id[{}] Connected room - [{}] user_name - [{}]]"
                , client.getSessionId().toString(), room, userName);
    }

    @OnDisconnect
    private void onDisconnected(SocketIOClient client) {
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        String room = String.valueOf(params.get("room").toArray()[0]);
        String userName = String.valueOf(params.get("userName").toArray()[0]);
        client.leaveRoom(room);
        Message message = Message.builder()
                .userName(userName)
                .room(room)
                .messageType(MessageType.SERVER)
                .content(String.format("good bye %s", userName))
                .build();
        socketService.sendMessage(room, "read_message", client, message);
//        socketService.saveInfoMessage(client, String.format("good bye %s", userName), room);
        log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
    }
}
