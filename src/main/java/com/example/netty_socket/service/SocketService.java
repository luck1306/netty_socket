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
    }
}
