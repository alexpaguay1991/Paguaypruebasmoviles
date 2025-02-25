package com.espe.banca_movil.controller;

import com.espe.banca_movil.model.Card;
import com.espe.banca_movil.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    // Obtener todas las tarjetas de un usuario
    @GetMapping("/{userId}")
    public ResponseEntity<List<Card>> getCardsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(cardService.getCardsByUserId(userId));
    }

    // Obtener una tarjeta por ID
    @GetMapping("/card/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }

    // Agregar una tarjeta
    @PostMapping("/{userId}")
    public ResponseEntity<Card> addCard(@PathVariable Long userId, @RequestBody Card card) {
        Card createdCard = cardService.addCard(userId, card);  // Pasamos el userId y la tarjeta
        return ResponseEntity.status(201).body(createdCard);  // Retornamos el código de estado 201 para creación
    }

    // Congelar tarjeta
    @PostMapping("/freeze/{id}")
    public ResponseEntity<Void> freezeCard(@PathVariable Long id) {
        cardService.freezeCard(id);
        return ResponseEntity.noContent().build();
    }

    // Eliminar tarjeta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();  // Código de respuesta para operación exitosa sin cuerpo
    }
}
