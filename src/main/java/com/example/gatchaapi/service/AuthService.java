package com.example.gatchaapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import com.example.gatchaapi.model.User;
import com.example.gatchaapi.repository.UserRepository;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    public String authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            String token = generateToken(username);
            User user = userOpt.get();
            user.setToken(token);
            user.setTokenExpiration(LocalDateTime.now().plusHours(1));
            userRepository.save(user);
            return token;
        }
        throw new RuntimeException("Invalid credentials");
    }

    public boolean validateToken(String token) {
        Optional<User> userOpt = userRepository.findByToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getTokenExpiration().isAfter(LocalDateTime.now())) {
                user.setTokenExpiration(LocalDateTime.now().plusHours(1));
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    private String generateToken(String username) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss");
        String dateTime = LocalDateTime.now().format(formatter);
        return username + "-" + dateTime + "-" + UUID.randomUUID().toString();
    }
}