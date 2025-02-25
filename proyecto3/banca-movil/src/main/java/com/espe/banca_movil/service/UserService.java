package com.espe.banca_movil.service;

import com.espe.banca_movil.model.User;
import com.espe.banca_movil.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }



    public Optional<User> login(String correo, String contraseña) {
        // Buscar el usuario por correo
        Optional<User> user = userRepository.findByCorreo(correo);

        if (user != null) {
            // Verificar la contraseña

            return user;
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }
}
