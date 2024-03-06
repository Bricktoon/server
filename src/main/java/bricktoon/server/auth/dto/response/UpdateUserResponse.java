package bricktoon.server.auth.dto.response;

import lombok.Builder;

@Builder
public record UpdateUserResponse(
        String accessToken
) {
}
