package org.example.springbootproject.service;

import org.example.springbootproject.entity.RefreshToken;
import org.example.springbootproject.entity.User;
import org.example.springbootproject.repository.RefreshTokenRepository;
import org.example.springbootproject.repository.UserRepository;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    public String generateNewAccessToken(String refreshToken) {
        RefreshToken refreshTokenObj = refreshTokenRepository.findRefreshTokenByToken(refreshToken);

        if (refreshTokenObj != null && !refreshTokenObj.isExpired()) {
            User user = refreshTokenObj.getUser();
            return authService.generateToken(user.getUsername(), false);
        }

        return null;
    }

    public RefreshToken saveRefreshToken(String refreshToken, String username) {
        RefreshToken refreshTokenObj = refreshTokenRepository.findRefreshTokenByUser_Username(username);

        if (refreshTokenObj == null) {
            User user = userRepository.findUserByUsername(username);
            refreshTokenObj = new RefreshToken();
            refreshTokenObj.setUser(user);
        }

        refreshTokenObj.setToken(refreshToken);
        refreshTokenObj.setExpiryDate(Instant.now().plusSeconds(Constants.REFRESH_TOKEN_EXPIRE_TIME));

        return refreshTokenRepository.save(refreshTokenObj);
    }
}
