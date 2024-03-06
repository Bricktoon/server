package bricktoon.server.auth.controller;

import bricktoon.server.auth.dto.request.LoginRequest;
import bricktoon.server.auth.service.AuthService;
import bricktoon.server.global.response.BaseResponse;
import bricktoon.server.global.response.BaseResponseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<BaseResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        return BaseResponse.toResponseEntityContainsResult(
                authService.login(loginRequest)
        );
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PatchMapping
    public ResponseEntity<BaseResponse> updateUser(
            UserDetails userDetails,
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        return BaseResponse.toResponseEntityContainsResult(authService.updateUser(userDetails, loginRequest));
    }
}
