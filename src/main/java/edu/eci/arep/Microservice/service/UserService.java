package edu.eci.arep.Microservice.service;

import edu.eci.arep.Microservice.dto.UserDTO;
import edu.eci.arep.Microservice.exception.UserNotFoundException;
import edu.eci.arep.Microservice.model.User;
import edu.eci.arep.Microservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.util.*;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    private UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return new ArrayList<>(userRepository.findAll());
    }

    public User getUserById(String id) throws UserNotFoundException{
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException(id);
        }
    }

    public User saveUser(UserDTO userDTO){
        User newUser = new User(userDTO);
        userRepository.save(newUser);
        return newUser;
    }

    public boolean auth(UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user == null) {
            return false;
        }
        return BCrypt.checkpw(userDto.getPassword(), user.getPassword());
    }

    public User updateUser(String id, UserDTO userDTO) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) throw new Exception(id);
        User updateUser = user.get();
        updateUser.update(userDTO);
        userRepository.save(updateUser);
        return updateUser;
    }

    public void deleteUser(String id) throws UserNotFoundException {
        if(!userRepository.existsById(id)) throw new UserNotFoundException(id);
        userRepository.deleteById(id);
    }
}