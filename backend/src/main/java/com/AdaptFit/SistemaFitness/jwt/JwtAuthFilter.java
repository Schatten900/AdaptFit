package com.AdaptFit.SistemaFitness.jwt;

import com.AdaptFit.SistemaFitness.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.annotation.PostConstruct;
import java.io.IOException;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostConstruct
    public void init() {
        System.out.println(">>> JwtAuthFilter initialized and registered");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();
        System.out.println(">>> JwtAuthFilter: Processing request: " + requestMethod + " " + requestUri);

        String token = extractToken(request);

        if (token != null) {
            System.out.println(">>> Token extracted: " + token.substring(0, Math.min(20, token.length())) + "...");

            if (!jwtService.validateToken(token)) {
                System.out.println(">>> Token validation failed");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            System.out.println(">>> Token validated successfully");

            String username = jwtService.extractUsername(token);
            System.out.println(">>> Username extracted: " + username);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println(">>> UserDetails loaded: " + userDetails.getClass().getName());

            if (userDetails instanceof User) {
                User user = (User) userDetails;
                System.out.println(">>> User instance created with ID: " + user.getId());

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );

                auth.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println(">>> Authentication set in SecurityContext");
            } else {
                System.out.println(">>> UserDetails is NOT instance of User: " + userDetails.getClass());
            }
        } else {
            System.out.println(">>> No token found in request");
        }

        System.out.println(">>> Calling filterChain.doFilter()");
        filterChain.doFilter(request, response);
        System.out.println(">>> filterChain.doFilter() completed");
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
