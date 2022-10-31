package com.example.netty_socket.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Message {
    private MessageType messageType;
    private String message;
    private String room;

    public Message(MessageType messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }
    public enum MessageType {
        SERVER, CLIENT
    }
}
