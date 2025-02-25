package com.espe.banca_movil.controller;

import com.espe.banca_movil.model.Payment;
import com.espe.banca_movil.model.User;
import com.espe.banca_movil.service.PaymentService;
import com.espe.banca_movil.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    // ✅ Endpoint para realizar un pago
    @PostMapping("/make")
    public ResponseEntity<?> makePayment(@RequestParam Long userId, @RequestParam Double amount) {
        // Buscar al usuario en la base de datos
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado.");
        }

        User user = userOptional.get();

        // Verificar saldo suficiente
        if (user.getSaldo() < amount) {
            return ResponseEntity.badRequest().body("Saldo insuficiente para realizar el pago.");
        }

        // Descontar saldo y registrar el pago
        user.setSaldo(user.getSaldo() - amount);
        userRepository.save(user); // Guardamos el nuevo saldo en la BD

        // Crear y guardar el pago
        Payment payment = new Payment();
        payment.setUsuarioId(userId);
        payment.setMonto(amount);
        payment.setFecha(LocalDateTime.now());

        Payment savedPayment = paymentService.makePayment(payment);
        return ResponseEntity.ok(savedPayment);
    }

    // ✅ Endpoint para obtener pagos por userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUserId(@PathVariable Long userId) {
        List<Payment> payments = paymentService.getPaymentsByUserId(userId);
        if (payments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(payments);
    }
}
