package com.github.ovinola.qorixteam.controller;

import com.github.ovinola.qorixteam.dto.ChatMessageRequest;
import com.github.ovinola.qorixteam.dto.ChatMessageResponse;
import com.github.ovinola.qorixteam.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j // Adicionando log para debug
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/sendMessage/{roomId}")
    public void sendMessage(
            @DestinationVariable String roomId, // Recebendo como String para evitar bloqueio silencioso
            @Payload ChatMessageRequest chatMessageRequest
    ) {
        log.info("📢 [WEBSOCKET] Requisição recebida na sala: {}", roomId);
        log.info("📦 [WEBSOCKET] Payload: senderId={}, content='{}'", chatMessageRequest.senderId(), chatMessageRequest.content());

        try {
            // Converte manualmente para garantir que não falhe na injeção do Spring
            UUID roomUuid = UUID.fromString(roomId);

            // Tenta salvar no banco
            ChatMessageResponse response = chatService.processAndSaveMessage(roomUuid, chatMessageRequest);
            log.info("✅ [WEBSOCKET] Mensagem salva no banco! Distribuindo para os clientes...");

            // Faz o broadcast
            messagingTemplate.convertAndSend("/topic/room/" + roomId, response);
            log.info("🚀 [WEBSOCKET] Broadcast realizado com sucesso para: /topic/room/{}", roomId);

        } catch (Exception e) {
            log.error("❌ [WEBSOCKET] Erro ao processar mensagem: ", e);
        }
    }
}