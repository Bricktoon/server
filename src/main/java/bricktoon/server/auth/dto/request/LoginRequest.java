package bricktoon.server.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(
        @NotBlank(message = "사용자 ID는 필수입니다")
        String username,
        @NotBlank(message = "비밀번호는 필수입니다")
        String password
) {
}
