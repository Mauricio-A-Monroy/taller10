package edu.eci.arep.microservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.eci.arep.microservice.model.Stream;

@Repository
public interface StreamRepository extends MongoRepository<Stream, String>{
}
