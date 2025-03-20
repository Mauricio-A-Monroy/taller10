package edu.eci.arep.Microservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import edu.eci.arep.Microservice.config.JwtUtil;
import edu.eci.arep.Microservice.dto.UserDTO;
import edu.eci.arep.Microservice.model.User;
import edu.eci.arep.Microservice.repository.UserRepository;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtil jwtUtils;
    @PostMapping("/signin")
    public String authenticateUser(@RequestBody UserDTO user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtils.generateToken(userDetails.getUsername());
    }

    @PostMapping("/signup")
    public String registerUser(@RequestBody UserDTO user) {
        if (userRepository.findByName(user.getName()) != null) {
            return "Error: Username is already taken!";
        }
        User newUser = new User( new UserDTO(
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                encoder.encode(user.getPassword()))
        );
        userRepository.save(newUser);
        return "User registered successfully!";
    }
}