package edu.eci.arep.microservice.controller;

import edu.eci.arep.microservice.config.JwtUtil;
import edu.eci.arep.microservice.dto.UserDTO;
import edu.eci.arep.microservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    @Test
    void testSignIn_Success() {
        // Arrange
        UserDTO userDTO = new UserDTO("testUser", "lastName", "test@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(jwtUtil.generateToken("test@example.com")).thenReturn("fakeToken");

        // Act
        String result = authController.authenticateUser(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals("fakeToken", result);
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtUtil, times(1)).generateToken("test@example.com");
    }

    @Test
    void testSignUp_Success() {
        // Arrange
        UserDTO userDTO = new UserDTO("newUser", "lastName", "new@example.com", "password");
        when(userRepository.findByName("newUser")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Act
        String result = authController.registerUser(userDTO);

        // Assert
        assertEquals("User registered successfully!", result);
        verify(userRepository, times(1)).findByName("newUser");
        verify(userRepository, times(1)).save(any());
    }
}