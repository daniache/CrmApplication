package org.manmet.crmapplication.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.manmet.crmapplication.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Check if the request URI starts with /api/ to restrict the filter to API paths
        /*String requestURI = request.getRequestURI();
        if (!requestURI.startsWith("/api/")) {
            // If it's not an API path, skip the JWT filter
            filterChain.doFilter(request, response);
            return;
        }*/

        /*String token = null;
        String authHeader = request.getHeader("Authorization");



        // First, try to get the token from the Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {*/
            // Extract JWT token from cookies only
            String token = null;
            // If there's no Authorization header, try to get the token from a cookie
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                        log.info("JWT token found in Cookie: {}", token);
                        break;
                    }
                }
            }


        if (token != null) {
            String username = jwtUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.info("User '{}' authenticated with authorities: {}", userDetails.getUsername(), userDetails.getAuthorities());
                } else {
                    log.warn("Invalid JWT token for user '{}'", username);
                }
            } else {
                log.warn("Username extracted from JWT token is null or already authenticated");
            }
        } else {
            log.warn("No JWT token found in Authorization header or cookies");
        }

        filterChain.doFilter(request, response);
    }
}
