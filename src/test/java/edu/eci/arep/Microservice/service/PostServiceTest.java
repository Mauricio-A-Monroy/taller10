package edu.eci.arep.Microservice.service;

import edu.eci.arep.Microservice.dto.PostDTO;
import edu.eci.arep.Microservice.exception.StreamNotFoundException;
import edu.eci.arep.Microservice.exception.UserException;
import edu.eci.arep.Microservice.model.Post;
import edu.eci.arep.Microservice.model.User;
import edu.eci.arep.Microservice.repository.PostRepository;
import edu.eci.arep.Microservice.service.UserService;
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

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePost_Success() throws UserException {
        // Arrange
        PostDTO postDTO = new PostDTO("creator", "content", "streamId");
        User user = new User("creator", "lastName", "creator@example.com", "password");

        when(userService.getUserByName("creator")).thenReturn(user);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        PostDTO result = postService.createPost(postDTO);

        // Assert
        assertNotNull(result);
        assertEquals("creator", result.getCreator());
        assertEquals("content", result.getContent());
        assertEquals("streamId", result.getStreamId());
        verify(userService, times(1)).getUserByName("creator");
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testCreatePost_UserNotFound() throws UserException {
        // Arrange
        PostDTO postDTO = new PostDTO("nonExistentUser", "content", "streamId");

        when(userService.getUserByName("nonExistentUser")).thenThrow(new UserException("User not found"));

        // Act & Assert
        assertThrows(UserException.class, () -> postService.createPost(postDTO));
        verify(userService, times(1)).getUserByName("nonExistentUser");
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void testGetPostById_Success() throws StreamNotFoundException {
        // Arrange
        String postId = "123";
        Post post = new Post("creator", "content", "streamId");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        PostDTO result = postService.getPostById(postId);

        // Assert
        assertNotNull(result);
        assertEquals("creator", result.getCreator());
        assertEquals("content", result.getContent());
        assertEquals("streamId", result.getStreamId());
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void testGetPostsByStreamId_Success() {
        // Arrange
        String streamId = "stream123";
        Post post = new Post("creator", "content", streamId);

        when(postRepository.findByStreamId(streamId)).thenReturn(Collections.singletonList(post));

        // Act
        List<Post> result = postService.getPostsByStreamId(streamId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("creator", result.get(0).getCreator());
        assertEquals("content", result.get(0).getContent());
        assertEquals(streamId, result.get(0).getStreamId());
        verify(postRepository, times(1)).findByStreamId(streamId);
    }
}