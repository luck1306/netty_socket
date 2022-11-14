package com.example.netty_socket.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.example.netty_socket.dto.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SocketService {

    public void sendMessage(String room, // the room for receiving message
                            String eventName, // event for receiving message
                            SocketIOClient senderClient, // person to want sending message
                            String message) { // message
        Object o = senderClient.getNamespace().getRoomOperations(room).getClients().stream()
                .filter(client -> !client.getSessionId().equals(senderClient.getSessionId()))
                .map(client -> {
                    client.sendEvent(eventName, new Message(Message.MessageType.SERVER, message));
                    return null;
                });
    }
}
