package org.example.springbootproject.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.springbootproject.payload.request.LoginRequest;
import org.example.springbootproject.payload.request.RegisterRequest;
import org.example.springbootproject.repository.UserRepository;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class AuthService extends BaseService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            org.example.springbootproject.entity.User user = userRepository.findUserByUsername(username);

            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole()).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    public void registerUser(RegisterRequest registerRequest) {
        org.example.springbootproject.entity.User user = new org.example.springbootproject.entity.User();
        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRole("USER");
        user.setEmail("chiendam12030@gmail.com");

        userRepository.save(user);
    }

    // Generate token with given user name
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userName);

        return createToken(claims, userName);
    }

    // Create a JWT token with specified claims and subject (username)
    private String createToken(Map<String, Object> claims, String userName) {
        try {
            long expMillis = System.currentTimeMillis() + Constants.TOKEN_EXPIRE_TIME;

           return Jwts.builder()
                    .claims(claims)
                    .issuer(userName)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(expMillis))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
        }catch (Exception e) {
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

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
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
