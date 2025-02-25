package com.espe.banca_movil.repository;

import com.espe.banca_movil.model.Card;
import com.espe.banca_movil.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByUserId(Long userId); // Buscar tarjetas por ID de usuario
}
