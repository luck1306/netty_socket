package com.example.netty_socket.entity.repository;

import com.example.netty_socket.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByRoom(String room);
}
