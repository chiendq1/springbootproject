package org.example.springbootproject.repository;

import org.example.springbootproject.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findRefreshTokenByToken(String refreshToken);
    RefreshToken findRefreshTokenByUser_Username(String userName);
}
