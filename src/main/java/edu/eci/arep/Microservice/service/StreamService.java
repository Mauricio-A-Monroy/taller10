package edu.eci.arep.Microservice.service;

import edu.eci.arep.Microservice.dto.StreamDTO;
import edu.eci.arep.Microservice.exception.StreamNotFoundException;
import edu.eci.arep.Microservice.model.Stream;
import edu.eci.arep.Microservice.model.User;
import edu.eci.arep.Microservice.repository.StreamRepository;
import edu.eci.arep.Microservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class StreamService {

    @Autowired
    private StreamRepository streamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostService postService;

    public List<Stream> getAllStreams() {
        return new ArrayList<>(streamRepository.findAll());
    }

    public StreamDTO createStream(StreamDTO streamDTO) throws Exception {
        // Validar que el creator no esté vacío
        if (streamDTO.getCreator() == null || streamDTO.getCreator().trim().isEmpty()) {
            throw new Exception("Creator is required");
        }

        // Validar que el creator exista en la base de datos
        User user = userRepository.findById(streamDTO.getCreator()).orElse(null);
        if (user == null) {
            throw new Exception("Creator does not exist");
        }

        Stream stream = new Stream(streamDTO.getCreator());
        stream = streamRepository.save(stream);
        return new StreamDTO(stream.getCreator());
    }

    public StreamDTO getStreamById(String id) throws StreamNotFoundException {
        Optional<Stream> optionalStream = streamRepository.findById(id);
        if (optionalStream.isEmpty()) {
            throw new StreamNotFoundException(id);
        }
        Stream stream = optionalStream.get();
        return new StreamDTO(stream.getCreator());
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