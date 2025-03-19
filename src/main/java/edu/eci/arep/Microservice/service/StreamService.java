package edu.eci.arep.Microservice.service;

import edu.eci.arep.Microservice.dto.PostDTO;
import edu.eci.arep.Microservice.dto.StreamDTO;
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
    private PostService postService;

    public List<Stream> getAllStreams() {
        return new ArrayList<>(streamRepository.findAll());
    }

    public StreamDTO createStream(StreamDTO streamDTO) {
        Stream stream = new Stream();
        stream.setCreator(streamDTO.getCreator());
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

}