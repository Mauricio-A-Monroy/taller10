package edu.eci.arep.Microservice.service;

import edu.eci.arep.Microservice.dto.PostDTO;
import edu.eci.arep.Microservice.dto.UserDTO;
import edu.eci.arep.Microservice.model.Post;
import edu.eci.arep.Microservice.model.Stream;
import edu.eci.arep.Microservice.model.User;
import edu.eci.arep.Microservice.repository.PostRepository;
import edu.eci.arep.Microservice.repository.StreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class StreamService {

    @Autowired
    private StreamRepository streamRepository;

    public List<Stream> getAllStreams(){
        return new ArrayList<>(streamRepository.findAll());
    }

    public Stream updateStream(String id, PostDTO postDTO) throws Exception {
        Stream stream = null;
        if (id.equals("0")){
            id = "";
            String user = postDTO.getUser();
            stream = new Stream();
            stream.setDate(LocalDate.now());
            stream.setCreator(user);
        }
        else {
            Optional<Stream> optionalStream = streamRepository.findById(id);
            if(optionalStream.isEmpty()){
                throw new Exception("Stream not found");
            }
            stream = optionalStream.get();
        }
        Post post = new Post(postDTO);
        List<Post> posts = (stream.getPosts() != null && !id.equals("0")) ? stream.getPosts() : new ArrayList<>();
        posts.add(post);
        stream.setPosts(posts);
        streamRepository.save(stream);
        return stream;
    }

}
