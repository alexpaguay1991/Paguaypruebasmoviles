package com.espe.banca_movil.controller;

import com.espe.banca_movil.model.User;
import com.espe.banca_movil.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Optional<User>> login(@RequestParam String correo, @RequestParam String contrasena) {
        return ResponseEntity.ok(userService.login(correo, contrasena));
    }

}
