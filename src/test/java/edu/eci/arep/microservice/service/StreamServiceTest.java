package edu.eci.arep.microservice.service;

import edu.eci.arep.microservice.dto.StreamDTO;
import edu.eci.arep.microservice.dto.StreamResponseDTO;
import edu.eci.arep.microservice.exception.StreamNotFoundException;
import edu.eci.arep.microservice.exception.UserException;
import edu.eci.arep.microservice.model.Post;
import edu.eci.arep.microservice.model.Stream;
import edu.eci.arep.microservice.model.User;
import edu.eci.arep.microservice.repository.StreamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StreamServiceTest {

    @Mock
    private StreamRepository streamRepository;

    @Mock
    private UserService userService;

    @Mock
    private PostService postService;

    @InjectMocks
    private StreamService streamService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStream_Success() throws Exception {
        // Arrange
        StreamDTO streamDTO = new StreamDTO("creator");
        User user = new User("creator", "lastName", "creator@example.com", "password");
        Stream stream = new Stream("creator");

        when(userService.getUserByName("creator")).thenReturn(user);
        when(streamRepository.save(any(Stream.class))).thenReturn(stream);
        when(postService.getPostsByStreamId(any())).thenReturn(Collections.emptyList());

        // Act
        StreamResponseDTO result = streamService.createStream(streamDTO);

        // Assert
        assertNotNull(result);
        assertEquals("creator", result.getCreator());
        verify(userService, times(1)).getUserByName("creator");
        verify(streamRepository, times(1)).save(any(Stream.class));
        verify(postService, times(1)).getPostsByStreamId(any());
    }

    @Test
    void testCreateStream_UserNotFound() throws UserException {
        // Arrange
        StreamDTO streamDTO = new StreamDTO("nonExistentUser");

        when(userService.getUserByName("nonExistentUser")).thenThrow(new UserException("User not found"));

        // Act & Assert
        assertThrows(UserException.class, () -> streamService.createStream(streamDTO));
        verify(userService, times(1)).getUserByName("nonExistentUser");
        verify(streamRepository, never()).save(any(Stream.class));
    }

    @Test
    void testGetStreamById_Success() throws StreamNotFoundException {
        // Arrange
        String streamId = "123";
        Stream stream = new Stream("creator");
        List<Post> posts = Collections.singletonList(new Post("creator", "content", streamId));

        when(streamRepository.findById(streamId)).thenReturn(Optional.of(stream));
        when(postService.getPostsByStreamId(streamId)).thenReturn(posts);

        // Act
        StreamResponseDTO result = streamService.getStreamById(streamId);

        // Assert
        assertNotNull(result);
        assertEquals("creator", result.getCreator());
        assertEquals(1, result.getPosts().size());
        verify(streamRepository, times(1)).findById(streamId);
        verify(postService, times(1)).getPostsByStreamId(streamId);
    }

    @Test
    void testGetStreamById_NotFound() {
        // Arrange
        String streamId = "456";

        when(streamRepository.findById(streamId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(StreamNotFoundException.class, () -> streamService.getStreamById(streamId));
        verify(streamRepository, times(1)).findById(streamId);
        verify(postService, never()).getPostsByStreamId(any());
    }

    @Test
    void testDeleteStream_Success() throws StreamNotFoundException {
        // Arrange
        String streamId = "123";
        Stream stream = new Stream("creator");

        when(streamRepository.findById(streamId)).thenReturn(Optional.of(stream));

        // Act
        streamService.deleteStream(streamId);

        // Assert
        verify(postService, times(1)).deletePostsByStreamId(streamId);
        verify(streamRepository, times(1)).deleteById(streamId);
    }

    @Test
    void testDeleteStream_NotFound() {
        // Arrange
        String streamId = "456";

        when(streamRepository.findById(streamId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(StreamNotFoundException.class, () -> streamService.deleteStream(streamId));
        verify(postService, never()).deletePostsByStreamId(any());
        verify(streamRepository, never()).deleteById(any());
    }
}