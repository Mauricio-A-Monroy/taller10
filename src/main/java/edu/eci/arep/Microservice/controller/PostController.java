package edu.eci.arep.Microservice.controller;

import edu.eci.arep.Microservice.dto.PostDTO;
import edu.eci.arep.Microservice.dto.StreamDTO;
import edu.eci.arep.Microservice.exception.StreamNotFoundException;
import edu.eci.arep.Microservice.model.Post;
import edu.eci.arep.Microservice.model.Stream;
import edu.eci.arep.Microservice.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

//    @GetMapping
//    public List<Stream> getAllPost() {
//        return postService.getAllPost();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<StreamDTO> getPost(@PathVariable String id) throws StreamNotFoundException {
//        PostDTO postDTO = postService.getPostById(id);
//        return ResponseEntity.ok(postDTO);
//    }

    @PostMapping
    public ResponseEntity<Post> createPost(@Valid @RequestBody PostDTO postDTO) throws Exception {
        Post post = postService.createPost(postDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }
}