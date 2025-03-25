package edu.eci.arep.microservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.eci.arep.microservice.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String>{
    List<Post> findByStreamId(String streamId);
    void deleteByStreamId(String streamId);
}
