package edu.eci.arep.microservice.service;

import edu.eci.arep.microservice.dto.StreamDTO;
import edu.eci.arep.microservice.dto.StreamResponseDTO;
import edu.eci.arep.microservice.exception.StreamNotFoundException;
import edu.eci.arep.microservice.exception.UserException;
import edu.eci.arep.microservice.model.Post;
import edu.eci.arep.microservice.model.Stream;
import edu.eci.arep.microservice.model.User;
import edu.eci.arep.microservice.repository.StreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class StreamService {


    private StreamRepository streamRepository;

    private UserService userService;

    private PostService postService;

    @Autowired
    public StreamService(StreamRepository streamRepository, UserService userService, PostService postService) {
        this.streamRepository = streamRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public List<Stream> getAllStreams() {
        return new ArrayList<>(streamRepository.findAll());
    }

    public StreamResponseDTO getStreamById(String id) throws StreamNotFoundException {
        Optional<Stream> optionalStream = streamRepository.findById(id);
        if (optionalStream.isEmpty()) {
            throw new StreamNotFoundException(id);
        }
        Stream stream = optionalStream.get();

        // Obtener los posts asociados
        List<Post> posts = postService.getPostsByStreamId(id);

        // Crear el StreamResponseDTO
        return new StreamResponseDTO(
                stream.getId(),
                stream.getCreator(),
                stream.getDate(),
                posts
        );
    }

    public StreamResponseDTO createStream(StreamDTO streamDTO) throws UserException {
        // Validar que el creator no esté vacío
        if (streamDTO.getCreator() == null || streamDTO.getCreator().trim().isEmpty()) {
            throw new UserException(UserException.CREATOR_REQUIRES);
        }


        User user = userService.getUserByName(streamDTO.getCreator());

        if (user == null){
            throw new UserException(UserException.USER_NOT_FOUND);
        }

        Stream stream = new Stream(streamDTO.getCreator());
        stream = streamRepository.save(stream);

        // Obtener los posts asociados (inicialmente vacíos)
        List<Post> posts = postService.getPostsByStreamId(stream.getId());

        return new StreamResponseDTO(
                stream.getId(),
                stream.getCreator(),
                stream.getDate(),
                posts
        );

    }

    public void deleteStream(String id) throws StreamNotFoundException {
        // Verificar si el Stream existe
        Optional<Stream> optionalStream = streamRepository.findById(id);
        if (optionalStream.isEmpty()) {
            throw new StreamNotFoundException(id);
        }

        // Eliminar todos los Posts asociados al Stream
        postService.deletePostsByStreamId(id);

        // Eliminar el Stream
        streamRepository.deleteById(id);
    }

}