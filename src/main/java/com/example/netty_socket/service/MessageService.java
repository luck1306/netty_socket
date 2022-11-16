package com.example.netty_socket.service;

import com.example.netty_socket.entity.Message;
import com.example.netty_socket.entity.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public List<Message> getMessages(String room) {
        return messageRepository.findAllByRoom(room);
    }

    public Message setMessage(Message message) {
        return messageRepository.save(message);
    }
}
