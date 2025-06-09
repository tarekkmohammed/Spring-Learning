package controller;


import model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/userContent")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public List<User> getAllUsers(Authentication authentication) {
        List<User> users= userRepository.findAll();
        System.out.println("Number of users found: " + users.size());
        Jwt jwt = (Jwt) authentication.getPrincipal();
        System.out.println("message"+ "Hello! Your token is valid 🎉");
        System.out.println("userId : "+ jwt.getSubject());
        System.out.println("userEmail: " + jwt.getClaimAsString("email"));
        System.out.println("tokenExpiry: "+ jwt.getExpiresAt());
        for (User user : users) {
            System.out.println("User ID: " + user.getId());
            System.out.println("Username: " + user.getUserName());
            System.out.println("Password: " + user.getPassword());
            System.out.println("User object: " + user.toString());
            System.out.println("---");
        }
        return users;
    }

}


