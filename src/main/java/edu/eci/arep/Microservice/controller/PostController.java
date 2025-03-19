package edu.eci.arep.Microservice.controller;

import edu.eci.arep.Microservice.dto.PostDTO;
import edu.eci.arep.Microservice.model.Stream;
import edu.eci.arep.Microservice.service.StreamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private StreamService streamService;

    @PostMapping
    public Stream createPost(@Valid @RequestBody PostDTO postDTO) throws Exception {
        return streamService.updateStream("0", postDTO);
    }
}