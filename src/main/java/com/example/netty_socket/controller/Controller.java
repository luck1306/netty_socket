package com.example.netty_socket.controller;

import com.example.netty_socket.dto.Message;
import com.example.netty_socket.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/message")
@RestController
public class Controller {
    private final MessageService messageService;

    @CrossOrigin
    @GetMapping("/{room}")
    public void getMessage(@PathVariable String room) {
    }
}
