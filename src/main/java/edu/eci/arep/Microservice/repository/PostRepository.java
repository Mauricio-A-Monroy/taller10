package edu.eci.arep.Microservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.eci.arep.Microservice.model.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String>{
    
}
