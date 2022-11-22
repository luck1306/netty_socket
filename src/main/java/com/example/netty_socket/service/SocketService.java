package com.example.netty_socket.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.example.netty_socket.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SocketService {

    public void sendMessage(String room, // the room for receiving message
                            String eventName, // event for receiving message
                            SocketIOClient senderClient, // person to want sending message
                            Message message) { // message
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if(!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent(eventName, message);
            }
        }
//        Object o = senderClient.getNamespace().getRoomOperations(room).getClients().stream()
//                .filter(client -> !client.getSessionId().equals(senderClient.getSessionId()))
//                .map(client -> {
//                    client.sendEvent(eventName, message);
//                    return null;
//                });
    }

    public void saveMessage(SocketIOClient client, Message message) {
        Message data = Message.builder()
                .messageType(Message.MessageType.CLIENT)
                .content(message.getContent())
                .room(message.getRoom())
                .userName(message.getUserName())
                .build();
        sendMessage(message.getRoom(), "read_message", client, data);
    }

    public void saveInfoMessage(SocketIOClient client, String message, String room) {
        Message storedMessage = Message.builder()
                .messageType(Message.MessageType.SERVER)
                .room(room)
                .content(message)
                .build();
        sendMessage(room, "read_message", client, storedMessage);
    }
}
