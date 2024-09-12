package org.example.springbootproject.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springbootproject.payload.response.ApiResponse;
import org.example.springbootproject.service.AuthService;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            // Retrieve the Authorization header
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;

            // Check if the header starts with "Bearer "
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7); // Extract token
                username = authService.extractUsername(token); // Extract username from token
            }

            // If the token is valid and no authentication is set in the context
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = authService.loadUserByUsername(username);

                // Validate token and set authentication
                if (authService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // Continue with the filter chain
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            handleException(response, HttpStatus.UNAUTHORIZED, "Token expired");
        } catch (JwtException ex) {
            handleException(response, HttpStatus.BAD_REQUEST, "Invalid token");
        } catch (Exception ex) {
            handleException(response, HttpStatus.INTERNAL_SERVER_ERROR, "An internal error occurred");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().contains("api/auth");
    }
    // Helper method to handle exceptions and set response
    private void handleException(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status.value());
        ApiResponse<?> apiResponse = new ApiResponse<>(false, message, null, status);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
