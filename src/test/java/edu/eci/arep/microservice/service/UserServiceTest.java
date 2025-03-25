package edu.eci.arep.microservice.service;

import edu.eci.arep.microservice.exception.UserException;
import edu.eci.arep.microservice.model.User;
import edu.eci.arep.microservice.repository.UserRepository;
import edu.eci.arep.microservice.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById_Success() throws UserException {
        // Arrange
        String userId = "123";
        User user = new User("testUser", "testLastName", "test@example.com", "password");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals("testUser", result.getName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        String userId = "456";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserException.class, () -> userService.getUserById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testSaveUser_Success() throws UserException {
        // Arrange
        UserDTO userDTO = new UserDTO("newUser", "newLastName", "new@example.com", "password");
        when(userRepository.findByName("newUser")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.saveUser(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals("newUser", result.getName());
        verify(userRepository, times(1)).findByName("newUser");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSaveUser_UsernameAlreadyExists() {
        // Arrange
        UserDTO userDTO = new UserDTO("existingUser", "lastName", "existing@example.com", "password");
        when(userRepository.findByName("existingUser")).thenReturn(new User());

        // Act & Assert
        assertThrows(UserException.class, () -> userService.saveUser(userDTO));
        verify(userRepository, times(1)).findByName("existingUser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAuth_Success() {
        // Arrange
        UserDTO userDTO = new UserDTO("testUser", "lastName", "test@example.com", "password");
        User user = new User("testUser", "lastName", "test@example.com", BCrypt.hashpw("password", BCrypt.gensalt()));
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        // Act
        boolean result = userService.auth(userDTO);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testAuth_Failure() {
        // Arrange
        UserDTO userDTO = new UserDTO("testUser", "lastName", "test@example.com", "wrongPassword");
        User user = new User("testUser", "lastName", "test@example.com", BCrypt.hashpw("password", BCrypt.gensalt()));
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        // Act
        boolean result = userService.auth(userDTO);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }
}