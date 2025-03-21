package edu.eci.arep.Microservice.service;

import edu.eci.arep.Microservice.dto.UserDTO;
import edu.eci.arep.Microservice.exception.UserException;
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

    public User getUserById(String id) throws UserException{
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserException(UserException.USER_NOT_FOUND);
        }
    }

    public User getUserByEmail(String email) throws UserException {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserException(UserException.USER_NOT_FOUND);
        }
    }

    public User getUserByName(String name) throws UserException{
        Optional<User> user = Optional.ofNullable(userRepository.findByName(name));
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserException(UserException.USER_NOT_FOUND);
        }
    }

    public User saveUser(UserDTO userDTO) throws UserException {
        User user = userRepository.findByName(userDTO.getName());
        if(user!=null){
            throw new UserException(UserException.USER_NAME_USED);
        }
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

    public User updateUser(String id, UserDTO userDTO) throws UserException {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) throw new UserException(UserException.USER_NOT_FOUND);
        User updateUser = user.get();
        updateUser.update(userDTO);
        userRepository.save(updateUser);
        return updateUser;
    }

    public void deleteUser(String id) throws UserException {
        if(!userRepository.existsById(id)) throw new UserException(UserException.USER_NOT_FOUND);
        userRepository.deleteById(id);
    }

    public User getUserByEmail(String email) throws UserException {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserException(UserException.USER_NOT_FOUND);
        }
    }
}

