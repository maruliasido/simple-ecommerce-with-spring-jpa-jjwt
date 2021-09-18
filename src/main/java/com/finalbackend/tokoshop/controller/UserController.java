package com.finalbackend.tokoshop.controller;

import com.finalbackend.tokoshop.entity.User;
import com.finalbackend.tokoshop.model.Register;
import com.finalbackend.tokoshop.repository.UserRepository;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    
    @Autowired
    UserRepository userRepository;
    
    private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
    
    
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Register register){
        User user = new User();
        user.setUsername(register.getUsername());
        user.setPassword(bcrypt.encode(register.getPassword()));
        
        userRepository.save(user);
        
        
       return ResponseEntity.ok("Registered");
    }
    
    @DeleteMapping("/person/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable (name="id")Integer id, Principal principal){
        Integer adminId = 4;
        Integer currentUserId = userRepository.getUserByUsername(principal.getName()).getId();
        User userToDelete = userRepository.findById(id).get();
        if(adminId == currentUserId){
            userRepository.delete(userToDelete);
            return ResponseEntity.ok("User has been deleted");
        }
        
        
        return ResponseEntity.badRequest().body("You are not an admin, you can't delete User");
    }
}