package com.espe.banca_movil.repository;

import com.espe.banca_movil.model.Payment;
import com.espe.banca_movil.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUser(User user);
    // Método para obtener pagos por userId
    List<Payment> findByUsuarioId(Long userId);
}
