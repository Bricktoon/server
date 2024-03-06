package bricktoon.server.auth.service;

import bricktoon.server.auth.dto.response.LoginResponse;
import bricktoon.server.auth.dto.response.UpdateUserResponse;
import bricktoon.server.global.response.BaseException;
import bricktoon.server.global.response.BaseResponseStatus;
import bricktoon.server.global.service.JwtService;
import bricktoon.server.user.domain.User;
import bricktoon.server.auth.dto.request.LoginRequest;
import bricktoon.server.user.repository.UserRepository;
import bricktoon.server.user.service.UserFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final JwtService jwtService;
    private final UserFindService userFindService;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsernameAndPassword(loginRequest.username(), loginRequest.password())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR));
        return LoginResponse.builder()
                .id(user.getId())
                .place(user.getPlace().getKey())
                .accessToken(jwtService.createAccessToken(user.getUsername()))
                .build();
    }

    public UpdateUserResponse updateUser(UserDetails userDetails, LoginRequest loginRequest) {
        User user = userFindService.findByUserDetails(userDetails);

        if (userRepository.existsByUsername(loginRequest.username()))
            throw new BaseException(BaseResponseStatus.USERNAME_ALREADY_EXISTS_ERROR);

        user.update(loginRequest.username(), loginRequest.password());
        return UpdateUserResponse.builder()
                .accessToken(jwtService.createAccessToken(loginRequest.username()))
                .build();
    }
}
