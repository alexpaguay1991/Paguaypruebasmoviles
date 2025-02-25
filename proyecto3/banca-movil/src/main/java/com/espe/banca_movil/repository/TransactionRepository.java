package com.espe.banca_movil.repository;

import com.espe.banca_movil.model.Payment;
import com.espe.banca_movil.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByPayment(Payment payment);
}
