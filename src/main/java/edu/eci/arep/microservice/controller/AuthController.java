package edu.eci.arep.microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import edu.eci.arep.microservice.config.JwtUtil;
import edu.eci.arep.microservice.dto.UserDTO;
import edu.eci.arep.microservice.model.User;
import edu.eci.arep.microservice.repository.UserRepository;
import org.owasp.encoder.Encode;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    PasswordEncoder encoder;
    JwtUtil jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder encoder, JwtUtil jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public Map<String, String> authenticateUser(@RequestBody UserDTO user) {
        // Validar y sanitizar el email antes de autenticar
        String email = validateAndSanitizeEmail(user.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        user.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Obtener el ID del usuario desde la base de datos
        User userFromDb = userRepository.findByEmail(username);
        if (userFromDb == null) {
            throw new IllegalArgumentException("User not found");
        }
        String userId = userFromDb.getId(); // Asumiendo que el modelo User tiene un campo ID

        String token = jwtUtils.generateToken(userId);

        // Devolver el token en un objeto JSON
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

    @PostMapping("/signup")
    public String registerUser(@RequestBody UserDTO user) {
        // Validar y sanitizar el email
        String email = validateAndSanitizeEmail(user.getEmail());

        // Verificar si el nombre de usuario ya está tomado
        if (userRepository.findByName(user.getName()) != null) {
            return "Error: Username is already taken!";
        }

        // Crear y guardar el usuario
        User newUser = new User(new UserDTO(
                user.getName(),
                user.getLastName(),
                email,
                encoder.encode(user.getPassword()))
        );
        userRepository.save(newUser);
        return "User registered successfully!";
    }

    // Método para validar y sanitizar el email
    private String validateAndSanitizeEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        // Expresión regular optimizada para el email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Sanitizar el email usando OWASP Java Encoder
        return Encode.forHtml(email);
    }

}