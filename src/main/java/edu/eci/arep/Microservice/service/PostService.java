package edu.eci.arep.Microservice.service;

import edu.eci.arep.Microservice.dto.PostDTO;
import edu.eci.arep.Microservice.model.Post;
import edu.eci.arep.Microservice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(PostDTO postDTO) throws Exception {
        Post post = new Post(postDTO);
        return postRepository.save(post);
    }

    public List<Post> getPostsByStreamId(String streamId) {
        return postRepository.findByStreamId(streamId);
    }

    public void deletePostsByStreamId(String streamId) {
        postRepository.deleteByStreamId(streamId); // Elimina todos los posts asociados al stream
    }
}