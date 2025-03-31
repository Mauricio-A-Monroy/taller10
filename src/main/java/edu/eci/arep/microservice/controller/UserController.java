 package edu.eci.arep.microservice.controller;

 import edu.eci.arep.microservice.dto.*;
 import edu.eci.arep.microservice.exception.UserException;
 import edu.eci.arep.microservice.model.*;
 import edu.eci.arep.microservice.service.UserService;
 import jakarta.validation.Valid;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;
 import java.net.URI;
 import java.util.List;
 import java.util.Map;

 @RestController()
 @RequestMapping("/user")
 public class UserController {

     private final UserService userService;
     private static String error = "error";
     private static String message = "message";
     private static String mailNotFound = "No existe un usuario con el email ";
     private static String idNotFound = "No existe un usuario con el id ";
     private static String nameUsed = "El nombre de usuario ya está en uso";
     private static String deleted = "Usuario eliminado correctamente";

     @Autowired
     public UserController(UserService userService) {
         this.userService = userService;
     }

     @GetMapping
     public ResponseEntity<List<User>> getAllUsers() {
         List<User> users = userService.getAllUsers();
         return ResponseEntity.ok(users);
     }

     @GetMapping("/email/{email}")
     public ResponseEntity<Object> findUserByEmail(@PathVariable("email") String email){
         try {
             User user = userService.getUserByEmail(email);
             return ResponseEntity.ok(user);
         } catch (UserException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND)
                     .body(Map.of(error, mailNotFound + email));
         }
     }
  
     @GetMapping("/{id}")
     public ResponseEntity<Object> findById(@PathVariable("id") String id){
         try {
             User user = userService.getUserById(id);
             return ResponseEntity.ok(user);
         } catch (UserException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND)
                     .body(Map.of(error, idNotFound + id));
         }
     }

     @PostMapping
     public ResponseEntity<Object> createUser(@Valid @RequestBody UserDTO userDTO) {
         try {
             User createdUser = userService.saveUser(userDTO);
             URI createdUserUri = URI.create("user/" + createdUser.getId());
             return ResponseEntity.created(createdUserUri).body(createdUser);
         } catch (UserException e) {
             return ResponseEntity.status(HttpStatus.CONFLICT)
                     .body(Map.of(error, nameUsed));
         }
     }

     @PutMapping("/{id}")
     public ResponseEntity<Object> updateUser(@PathVariable("id") String id, @RequestBody UserDTO userDTO){
         try {
             User updatedUser = userService.updateUser(id, userDTO);
             return ResponseEntity.ok(updatedUser);
         } catch (UserException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND)
                     .body(Map.of(error, idNotFound + id));
         }
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("id") String id) {
         try {
             userService.deleteUser(id);
             return ResponseEntity.ok(Map.of(message, deleted));
         } catch (UserException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND)
                     .body(Map.of(error, idNotFound + id));
         }
     }

 }
