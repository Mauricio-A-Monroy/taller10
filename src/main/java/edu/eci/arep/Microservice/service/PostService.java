package edu.eci.arep.Microservice.service;

import edu.eci.arep.Microservice.dto.PostDTO;
import edu.eci.arep.Microservice.exception.PostNotFoundException;
import edu.eci.arep.Microservice.exception.StreamNotFoundException;
import edu.eci.arep.Microservice.model.Post;
import edu.eci.arep.Microservice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(PostDTO postDTO) throws Exception {
        Post post = new Post(postDTO);
        return postRepository.save(post);
    }

    public PostDTO getPostById(String id) throws StreamNotFoundException {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            throw new PostNotFoundException(id);
        }
        Post post = optionalPost.get();
        return new PostDTO(post.getUser(),post.getContent(),post.getStreamId());
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