package com.github.ovinola.qorixteam.dto;

import java.util.UUID;

public record ChatMessageRequest(
        UUID senderId,
        String content
) {
}
