package edu.eci.arep.microservice.service;

import edu.eci.arep.microservice.dto.PostDTO;
import edu.eci.arep.microservice.exception.PostNotFoundException;
import edu.eci.arep.microservice.exception.StreamNotFoundException;
import edu.eci.arep.microservice.exception.UserException;
import edu.eci.arep.microservice.model.Post;
import edu.eci.arep.microservice.model.User;
import edu.eci.arep.microservice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    public PostDTO createPost(PostDTO postDTO) throws UserException {
        try {
            User user = userService.getUserByName(postDTO.getCreator());
        } catch (UserException e) {
            throw new UserException(UserException.USER_NOT_FOUND);
        }

        Post post = new Post(postDTO);
        post = postRepository.save(post);
        return new PostDTO(post.getCreator(),post.getContent(),post.getStreamId());
    }

    public PostDTO getPostById(String id) throws StreamNotFoundException {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            throw new PostNotFoundException(id);
        }
        Post post = optionalPost.get();
        return new PostDTO(post.getCreator(),post.getContent(),post.getStreamId());
    }

    //Obtener todos los post asociados a un stream especifico
    public List<Post> getPostsByStreamId(String streamId) {
        return postRepository.findByStreamId(streamId);
    }

    //Eliminar todos los post asociados a un stream especifico
    public void deletePostsByStreamId(String streamId) {
        postRepository.deleteByStreamId(streamId); // Elimina todos los posts asociados al stream
    }
}