package com.github.ovinola.qorixteam.service;

import com.github.ovinola.qorixteam.dto.ChatMessageRequest;
import com.github.ovinola.qorixteam.dto.ChatMessageResponse;
import com.github.ovinola.qorixteam.model.AppUser;
import com.github.ovinola.qorixteam.model.ChatMessage;
import com.github.ovinola.qorixteam.model.ChatRoom;
import com.github.ovinola.qorixteam.repository.AppUserRepository;
import com.github.ovinola.qorixteam.repository.ChatMessageRepository;
import com.github.ovinola.qorixteam.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository messageRepository;
    private final AppUserRepository userRepository;
    private final ChatRoomRepository roomRepository;

    @Transactional
    public ChatMessageResponse processAndSaveMessage(UUID roomId, ChatMessageRequest request) {

        AppUser sender = userRepository.findById(request.senderId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ChatRoom room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        ChatMessage message = ChatMessage.builder()
                .sender(sender)
                .room(room)
                .content(request.content())
                .build();

        ChatMessage savedMessage = messageRepository.save(message);

        return new ChatMessageResponse(
                savedMessage.getId(),
                sender.getUsername(),
                savedMessage.getContent(),
                savedMessage.getTimestamp()
        );

    }

    public List<ChatMessageResponse> getRoomHistory(UUID roomId) {
        roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        return messageRepository.findByRoomIdOrderByTimestampAsc(roomId)
                .stream()
                .map(msg -> new ChatMessageResponse(
                        msg.getId(),
                        msg.getSender().getUsername(),
                        msg.getContent(),
                        msg.getTimestamp()
                ))
                .toList();
    }

}
