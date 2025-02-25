package com.espe.banca_movil.service;

import com.espe.banca_movil.model.Card;
import com.espe.banca_movil.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    // Obtener las tarjetas por el ID del usuario
    public List<Card> getCardsByUserId(Long userId) {
        return cardRepository.findByUserId(userId);
    }

    // Agregar una tarjeta
    public Card addCard(Long userId, Card card) {
        card.setUserId(userId);  // Asocia la tarjeta con el usuario
        return cardRepository.save(card);
    }

    // Congelar tarjeta
    public void freezeCard(Long cardId) {
        Optional<Card> cardOptional = cardRepository.findById(cardId);
        if (cardOptional.isPresent()) {
            Card card = cardOptional.get();
            card.setFrozen(true);  // Marcar la tarjeta como congelada
            cardRepository.save(card);
        }
    }

    // Eliminar tarjeta
    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }

    public Card getCardById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }
}
