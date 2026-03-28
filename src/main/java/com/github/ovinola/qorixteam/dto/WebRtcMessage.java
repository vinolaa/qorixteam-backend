package com.github.ovinola.qorixteam.dto;

import java.util.UUID;

public record WebRtcMessage(
        String type,
        UUID senderId,
        UUID targetId,
        String data
) {
}
