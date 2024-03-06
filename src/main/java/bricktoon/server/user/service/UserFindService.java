package bricktoon.server.user.service;

import bricktoon.server.global.response.BaseException;
import bricktoon.server.global.response.BaseResponseStatus;
import bricktoon.server.user.domain.User;
import bricktoon.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserFindService {
    private final UserRepository userRepository;

    public User findByUserDetails(UserDetails userDetails) {
        return userRepository.findByUsernameAndPassword(userDetails.getUsername(), userDetails.getPassword())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.MEMBER_NOT_FOUND_ERROR));
    }
}
