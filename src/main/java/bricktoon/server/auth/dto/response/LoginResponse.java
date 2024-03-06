package bricktoon.server.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(
        Long id,
        String place,
        String accessToken
) {
}
