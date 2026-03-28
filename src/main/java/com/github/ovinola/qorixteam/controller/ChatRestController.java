package com.github.ovinola.qorixteam.controller;

import com.github.ovinola.qorixteam.dto.ChatMessageResponse;
import com.github.ovinola.qorixteam.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chatService;

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getRoomHistory(@PathVariable UUID roomId) {
        List<ChatMessageResponse> history = chatService.getRoomHistory(roomId);
        return ResponseEntity.ok(history);
    }

}
