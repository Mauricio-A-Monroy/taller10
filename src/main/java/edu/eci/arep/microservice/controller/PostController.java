package edu.eci.arep.microservice.controller;

import edu.eci.arep.microservice.dto.PostDTO;
import edu.eci.arep.microservice.exception.StreamNotFoundException;
import edu.eci.arep.microservice.exception.UserException;
import edu.eci.arep.microservice.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable String id) throws StreamNotFoundException {
        PostDTO postDTO = postService.getPostById(id);
        return ResponseEntity.ok(postDTO);
    }

    @PostMapping
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO){
        try {
            PostDTO postCreated = postService.createPost(postDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(postCreated);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe un usuario con el nombre: " + postDTO.getCreator()));
        }
    }
}