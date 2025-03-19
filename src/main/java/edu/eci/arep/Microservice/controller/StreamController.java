package edu.eci.arep.Microservice.controller;

import edu.eci.arep.Microservice.dto.PostDTO;
import edu.eci.arep.Microservice.dto.StreamDTO;
import edu.eci.arep.Microservice.exception.StreamNotFoundException;
import edu.eci.arep.Microservice.model.Stream;
import edu.eci.arep.Microservice.service.StreamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public ResponseEntity<StreamDTO> getStream(@PathVariable String id) throws StreamNotFoundException {
        StreamDTO streamDTO = streamService.getStreamById(id);
        return ResponseEntity.ok(streamDTO);
    }

    @PostMapping
    public ResponseEntity<StreamDTO> createStream(@Valid @RequestBody StreamDTO streamDTO) {
        StreamDTO createdStreamDTO = streamService.createStream(streamDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStreamDTO);
    }

}