package edu.eci.arep.microservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import edu.eci.arep.microservice.repository.UserRepository;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscar el usuario por email en lugar de name
        edu.eci.arep.microservice.model.User user = userRepository.findByEmail(email);
        
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found with email: " + email);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),  // Usar email como identificador
                user.getPassword(), // Contraseña encriptada en la base de datos
                Collections.emptyList() // Roles (si los tienes, agrégalos aquí)
        );
    }
}
