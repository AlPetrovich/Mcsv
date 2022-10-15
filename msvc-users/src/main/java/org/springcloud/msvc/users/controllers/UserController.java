package org.springcloud.msvc.users.controllers;

import org.springcloud.msvc.users.models.entity.User;
import org.springcloud.msvc.users.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UserController {

    @Autowired
    private IUserService iUserService;

    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        return new ResponseEntity<>(iUserService.findAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id){
        Optional<User> userOptional = iUserService.findById(id);
        if ( userOptional.isPresent() ){
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult result){
        if(result.hasErrors()){
            return validations(result);
        }
        if( iUserService.existsUserByEmail(user.getEmail())){
            return ResponseEntity.badRequest()
                    .body(Collections
                            .singletonMap("Error", "Ya existe un usuario con ese correo"));
        }
        return new ResponseEntity<>(iUserService.saveUser(user), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user,BindingResult result,@PathVariable Long id){
        if(result.hasErrors()){
            return validations(result);
        }
        Optional<User> userData = iUserService.findById(id);
        if (userData.isPresent()){
            User userDb = userData.get();
            if( !user.getEmail().isEmpty() && !user.getEmail().equalsIgnoreCase( userDb.getEmail() )
                    && iUserService.findByEmail(user.getEmail()).isPresent()){
                return ResponseEntity.badRequest()
                        .body(Collections
                                .singletonMap("Error", "Ya existe un usuario con ese correo"));
            }
            userDb.setName(user.getName());
            userDb.setEmail(user.getEmail());
            userDb.setPassword(user.getPassword());
            return new ResponseEntity<>(iUserService.saveUser(userDb), HttpStatus.CREATED);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        Optional<User> userOptional = iUserService.findById(id);
        if ( userOptional.isPresent() ){
            iUserService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/users-per-course")
    public ResponseEntity<?> getAllUsersById(@RequestParam List<Long> ids){
        return ResponseEntity.ok(iUserService.findAllById(ids));
    }

    //metodo de validaciones
    private static ResponseEntity<Map<String, String>> validations(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo "+ err.getField()+ " "+ err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
