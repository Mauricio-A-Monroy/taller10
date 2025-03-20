package edu.eci.arep.Microservice.controller;

import edu.eci.arep.Microservice.dto.PostDTO;
import edu.eci.arep.Microservice.dto.StreamDTO;
import edu.eci.arep.Microservice.dto.StreamResponseDTO;
import edu.eci.arep.Microservice.exception.StreamNotFoundException;
import edu.eci.arep.Microservice.exception.UserException;
import edu.eci.arep.Microservice.model.Stream;
import edu.eci.arep.Microservice.service.StreamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stream")
public class StreamController {

    @Autowired
    StreamService streamService;

    //Consulta todos los streams creados y sus post asociados
    @GetMapping
    public List<Stream> getAllStreams() {
        return streamService.getAllStreams();
    }

    //Consulta todos los post asociados a un stream especifico
    @GetMapping("/{id}")
    public ResponseEntity<StreamResponseDTO> getStream(@PathVariable String id) throws StreamNotFoundException {
        StreamResponseDTO streamDTO = streamService.getStreamById(id);
        return ResponseEntity.ok(streamDTO);
    }

    @PostMapping
    public ResponseEntity<Object> createStream(@Valid @RequestBody StreamDTO streamDTO){
        try {
            StreamResponseDTO createdStreamDTO = streamService.createStream(streamDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStreamDTO);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe un usuario con el nombre: " + streamDTO.getCreator()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStream(@PathVariable String id) {
        try {
            streamService.deleteStream(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (StreamNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

}