package edu.eci.arep.Microservice.controller;

import edu.eci.arep.Microservice.dto.PostDTO;
import edu.eci.arep.Microservice.model.Stream;
import edu.eci.arep.Microservice.service.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stream")
public class StreamController {

    @Autowired
    StreamService streamService;

    @GetMapping
    public List<Stream> getAllPosts(){
        return streamService.getAllStreams();
    }

    @PostMapping("/{id}")
    public void addPost(@RequestBody PostDTO postDTO, @PathVariable("id") String id) throws Exception {
        streamService.updateStream(id, postDTO);
    }

}
