import 'dart:convert';
import 'package:http/http.dart' as http;

import '../modelos/card.dart';

class CardController {
  final String baseUrl = 'http://localhost:8080/api/cards';

  // Obtener las tarjetas de un usuario
  Future<List<CreditCard>> fetchCards(String userId) async {
    final response = await http.get(Uri.parse('$baseUrl/$userId'));

    // Agregar un mensaje para saber si la consulta fue exitosa
    if (response.statusCode == 200) {
      print('Consulta exitosa');
      print('Respuesta del servidor: ${response.body}');

      final List<dynamic> data = json.decode(response.body);
      return data.map((card) => CreditCard.desdeJson(card)).toList();
    } else {
      print('Error en la consulta. Código de estado: ${response.statusCode}');
      throw Exception('Error al obtener las tarjetas');
    }
  }
  // Obtener una tarjeta por ID
  Future<CreditCard> fetchCardById(String cardId) async {
    final response = await http.get(Uri.parse('$baseUrl/card/$cardId'));

    if (response.statusCode == 200) {
      print('Tarjeta obtenida exitosamente');
      return CreditCard.desdeJson(json.decode(response.body));
    } else {
      print('Error al obtener la tarjeta. Código de estado: ${response.statusCode}');
      throw Exception('Error al obtener la tarjeta');
    }
  }

  // Congelar una tarjeta
  Future<void> freezeCard(String cardId) async {
    final response = await http.post(Uri.parse('$baseUrl/freeze/$cardId'));
    if (response.statusCode != 204) { // Cambia el código de estado esperado a 204
      print('Error al congelar la tarjeta. Código de estado: ${response.statusCode}');
      throw Exception('Error al congelar la tarjeta');
    } else {
      print('Tarjeta congelada exitosamente');
    }
  }


  // Eliminar una tarjeta
  Future<void> deleteCard(String cardId) async {
    final response = await http.delete(Uri.parse('$baseUrl/$cardId'));
    if (response.statusCode != 204) {
      print('Error al eliminar la tarjeta. Código de estado: ${response.statusCode}');
      throw Exception('Error al eliminar la tarjeta ${response.statusCode}');
    } else {
      print('Tarjeta eliminada exitosamente');
    }
  }

  // Agregar una tarjeta
  Future<void> addCard(String userId, CreditCard card) async {
    final response = await http.post(
      Uri.parse('$baseUrl/$userId'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode(card.aJson()), // Suponiendo que el modelo tiene un método aJson()
    );

    if (response.statusCode != 201) {
      print('Error al agregar la tarjeta. Código de estado: ${response.statusCode}');
      throw Exception('Error al agregar la tarjeta');
    } else {
      print('Tarjeta agregada exitosamente');
    }
  }
}
