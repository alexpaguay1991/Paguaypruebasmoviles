package com.espe.banca_movil.service;

import com.espe.banca_movil.model.Payment;
import com.espe.banca_movil.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;


    // Método para realizar el pago (ya proporcionado)
    public Payment makePayment(Payment payment) {
        System.out.println(payment.getUsuarioId());
        System.out.println("si esta ingreaando" +payment.getTarjetaId());
        return paymentRepository.save(payment);
    }

    // Nuevo método para obtener los pagos por userId
    public List<Payment> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUsuarioId(userId);
    }
}
