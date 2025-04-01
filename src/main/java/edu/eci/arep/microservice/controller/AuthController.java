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
    public String authenticateUser(@RequestBody UserDTO user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Validar el username (email) antes de usarlo
        if (!isValidEmail(username)) {
            throw new IllegalArgumentException("Invalid username format");
        }

        return jwtUtils.generateToken(username);
    }

    @PostMapping("/signup")
    public String registerUser(@RequestBody UserDTO user) {
        // Validar el email
        String email = user.getEmail();
        if (!isValidEmail(email)) {
            return "Error: Invalid email format!";
        }

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

    // Método para validar el formato del email
    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        // Expresión regular para validar el formato del email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
}