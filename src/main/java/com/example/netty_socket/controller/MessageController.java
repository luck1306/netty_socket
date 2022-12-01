package com.example.netty_socket.controller;

import com.example.netty_socket.entity.Message;
import com.example.netty_socket.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/message")
@RestController
public class MessageController {
    private final MessageService messageService;

    @CrossOrigin
    @GetMapping("/{room}")
    public List<Message> getMessages(@PathVariable String room) {
        return messageService.getMessages(room);
    }

    @CrossOrigin
    @PostMapping
    public Message setMessage(@RequestBody Message message) {
        return messageService.setMessage(message);
    }
}
