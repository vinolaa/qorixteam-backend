package com.github.ovinola.qorixteam.controller;

import com.github.ovinola.qorixteam.dto.WebRtcMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SignalingController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Intercepta mensagens de sinalização enviadas para: /app/signaling/{roomId}
     * Como a sinalização não precisa ser salva no banco (é efêmera), 
     * nós apenas recebemos e repassamos imediatamente para a sala.
     */
    @MessageMapping("/signaling/{roomId}")
    public void handleSignaling(
            @DestinationVariable String roomId,
            @Payload WebRtcMessage message
    ) {
        log.info("📡 [WEBRTC] Sinalização recebida na sala {}: Tipo [{}] de {} para {}",
                roomId, message.type(), message.senderId(), message.targetId());

        // Repassa a mensagem para o tópico específico de sinalização da sala.
        // O frontend vai se inscrever em /topic/room/{roomId}/signaling
        // Lá no frontend, o JS vai verificar se o targetId bate com o ID dele.
        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/signaling", message);
    }
}