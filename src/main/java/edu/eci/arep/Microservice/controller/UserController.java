 package edu.eci.arep.Microservice.controller;

 import edu.eci.arep.Microservice.dto.*;
 import edu.eci.arep.Microservice.model.*;
 import edu.eci.arep.Microservice.service.UserService;
 import jakarta.validation.Valid;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;
 import java.net.URI;
 import java.util.List;

 @RestController()
 @RequestMapping("/user")
 public class UserController {

     private final UserService userService;

     @Autowired
     public UserController(UserService userService) {
         this.userService = userService;
     }

     @GetMapping
     public ResponseEntity<List<User>> getAllUsers() {
         List<User> users = userService.getAllUsers();
         return ResponseEntity.ok(users);
     }

     @GetMapping("/{id}")
     public ResponseEntity<User> findById(@PathVariable("id") String id) throws Exception {
         User user = userService.getUserById(id);
         return ResponseEntity.ok(user);
     }

     @PostMapping
     public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) {
         User createdUser = userService.saveUser(userDTO);
         URI createdUserUri = URI.create("user/" + createdUser.getId());
         return ResponseEntity.created(createdUserUri).body(createdUser);
     }

     @PutMapping("/{id}")
     public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody UserDTO userDTO) throws Exception {
         User updatedUser = userService.updateUser(id, userDTO);
         return ResponseEntity.ok(updatedUser);
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
         userService.deleteUser(id);
         return ResponseEntity.ok(null);
     }
 }