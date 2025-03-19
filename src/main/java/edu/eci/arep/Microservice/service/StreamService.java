package edu.eci.arep.Microservice.service;

import edu.eci.arep.Microservice.dto.PostDTO;
import edu.eci.arep.Microservice.exception.StreamNotFoundException;
import edu.eci.arep.Microservice.model.Post;
import edu.eci.arep.Microservice.model.Stream;
import edu.eci.arep.Microservice.repository.StreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class StreamService {

    @Autowired
    private StreamRepository streamRepository;

    @Autowired
    private PostService postService; // Inyectar PostService

    public List<Stream> getAllStreams() {
        return new ArrayList<>(streamRepository.findAll());
    }

    public Stream updateStream(String id, PostDTO postDTO) throws StreamNotFoundException, Exception {
        Stream stream;
        if (id.equals("0")) {
            stream = new Stream();
            stream.setDate(LocalDate.now());
            stream.setCreator(postDTO.getUser());
        } else {
            Optional<Stream> optionalStream = streamRepository.findById(id);
            if (optionalStream.isEmpty()) {
                throw new StreamNotFoundException(id);
            }
            stream = optionalStream.get();
        }

        // Delegar la creación del post a PostService
        Post post = postService.createPost(postDTO);
        post.setStreamId(stream.getId()); // Asignar el ID del stream al post

        List<Post> posts = (stream.getPosts() != null && !id.equals("0")) ? stream.getPosts() : new ArrayList<>();
        posts.add(post);
        stream.setPosts(posts);
        streamRepository.save(stream);
        return stream;
    }
}