package edu.eci.arep.microservice.config;

import edu.eci.arep.microservice.exception.UserException;
import edu.eci.arep.microservice.model.User;
import edu.eci.arep.microservice.repository.UserRepository;
import edu.eci.arep.microservice.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtils;
    private CustomUserDetailsService userDetailsService;
    private UserRepository userRepository;

    public AuthTokenFilter() {
    }

    @Autowired
    public AuthTokenFilter(JwtUtil jwtUtils, CustomUserDetailsService userDetailsService, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // Obtener el userId del token
                String userId = jwtUtils.getUserIdFromToken(jwt);

                // Buscar el usuario en la base de datos usando el userId
                Optional<User> user1 = userRepository.findById(userId);
                if(user1.isEmpty()) throw new UserException(UserException.USER_NOT_FOUND);
                User user = user1.get();

                // Obtener el username (email) del usuario
                String username = user.getEmail();

                // Cargar los detalles del usuario usando el username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.info("Cannot set user authentication: " + e);
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}