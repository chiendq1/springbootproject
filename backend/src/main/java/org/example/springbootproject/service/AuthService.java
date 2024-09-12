package org.example.springbootproject.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.springbootproject.entity.Role;
import org.example.springbootproject.payload.request.LoginRequest;
import org.example.springbootproject.payload.request.RegisterRequest;
import org.example.springbootproject.repository.UserRepository;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AuthService extends BaseService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            org.example.springbootproject.entity.User user = userRepository.findUserByUsername(username);

            // Get roles and convert them to GrantedAuthority
            Set<Role> roles = user.getRoles();
            List<GrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                    .collect(Collectors.toList());

            // Return UserDetails with authorities (roles)
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(authorities)
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    // Generate token with given user name
    public String generateToken(String userName, boolean isRefreshToken) {
        Map<String, Object> claims = new HashMap<>();
        org.example.springbootproject.entity.User user = userRepository.findUserByUsername(userName);

        if(user.isExist(userName)) {
            claims.put("sub", userName);
            claims.put("exp", new Date(System.currentTimeMillis() + Constants.REFRESH_TOKEN_EXPIRE_TIME));

            if (!isRefreshToken) {
                Set<Role> roles = user.getRoles();
                List<String> roleNames = roles.stream()
                        .map(Role::getRoleName)
                        .toList();
                claims.put("roles", roleNames);
                claims.put("exp", new Date(System.currentTimeMillis() + Constants.TOKEN_EXPIRE_TIME));
            }
        }

        return createToken(claims, userName);
    }

    // Create a JWT token with specified claims and subject (username)
    private String createToken(Map<String, Object> claims, String userName) {
        try {
            return Jwts.builder()
                    .claims(claims)
                    .issuer(userName)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .signWith(getSignKey())
                    .compact();
        } catch (Exception e) {
            logger.error(e.getMessage());

            return null;
        }
    }

    // Get the signing key for JWT token
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(Constants.SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract the username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate the token against user details and expiration
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        boolean result = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        return result;
    }

    public Authentication authenticate(LoginRequest loginRequest, AuthenticationManager authenticationManager) {
        try {
            Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword());

            return authenticationManager.authenticate(authenticationRequest);
        } catch (AuthenticationException e) {
            logger.error(e.getMessage());

            return null;
        }
    }
}
