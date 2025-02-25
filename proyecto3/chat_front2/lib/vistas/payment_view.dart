import 'package:flutter/material.dart';
import '../controladores/card_controller.dart';
import '../controladores/payment_controller.dart';
import '../modelos/card.dart'; // Suponiendo que tienes un modelo Card para las tarjetas

class PaymentScreen extends StatefulWidget {
  final String usuarioId; // Recibimos el usuarioId desde la pantalla principal.

  PaymentScreen({required this.usuarioId});

  @override
  _PaymentScreenState createState() => _PaymentScreenState();
}

class _PaymentScreenState extends State<PaymentScreen> {
  final PaymentController _paymentController = PaymentController();
  final CardController _cardController = CardController();
  final TextEditingController _montoController = TextEditingController();
  String? _selectedCardId; // Variable para almacenar el ID de la tarjeta seleccionada
  List<CreditCard> _cards = []; // Lista de tarjetas disponibles para el usuario

  @override
  void initState() {
    super.initState();
    _fetchUserCards();
  }

  // Método para obtener las tarjetas disponibles para el usuario
  void _fetchUserCards() async {
    try {
      final cards = await _cardController.fetchCards(widget.usuarioId); // Método que devuelve las tarjetas del usuario
      setState(() {
        _cards = cards;
      });
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error al cargar las tarjetas: $e')),
      );
    }
  }

  void _showPaymentConfirmation() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('Confirmación de Pago'),
          content: Text(
              '¿Desea confirmar el pago de \$${_montoController.text} con la tarjeta ${_selectedCardId ?? 'Seleccionar tarjeta'}?'),
          actions: <Widget>[
            TextButton(
              onPressed: () {
                Navigator.of(context).pop(); // Cerrar el diálogo
              },
              child: Text('Cancelar'),
            ),
            TextButton(
              onPressed: () {
                _makePayment(); // Realizar el pago
                Navigator.of(context).pop(); // Cerrar el diálogo
              },
              child: Text('Confirmar'),
            ),
          ],
        );
      },
    );
  }

  void _makePayment() async {
    try {
      double monto = double.parse(_montoController.text);
      String tarjetaId = _selectedCardId ?? ''; // Usar la tarjeta seleccionada
      await _paymentController.makePayment(monto, tarjetaId, widget.usuarioId);
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Pago realizado')),
      );
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error al realizar el pago: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Realizar Pago')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            TextField(
              controller: _montoController, // Controlador de monto
              decoration: InputDecoration(labelText: 'Monto'),
              keyboardType: TextInputType.number,
            ),
            SizedBox(height: 20),
            // Dropdown para seleccionar la tarjeta
            DropdownButton<String>(
              value: _selectedCardId,
              hint: Text('Selecciona una tarjeta'),
              onChanged: (String? newCardId) {
                setState(() {
                  _selectedCardId = newCardId;
                });
              },
              items: _cards.map<DropdownMenuItem<String>>((CreditCard card) {
                return DropdownMenuItem<String>(
                  value: card.id, // Suponiendo que Card tiene un campo 'id'
                  child: Text('Tarjeta: ${card.cardNumber}'), // Suponiendo que Card tiene un campo 'cardNumber'
                );
              }).toList(),
            ),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: _showPaymentConfirmation, // Mostrar la confirmación
              child: Text('Pagar'),
            ),
          ],
        ),
      ),
    );
  }
}
